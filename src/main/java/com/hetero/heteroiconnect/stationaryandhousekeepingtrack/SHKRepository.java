package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class SHKRepository {
	private JdbcTemplate jdbcTemplate;

	public SHKRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Map<String, Object>> getItems() {
		String query = "SELECT * FROM stationary_management.tbl_master_item_types WHERE status=1001";
		return jdbcTemplate.queryForList(query);
	}

	public List<Map<String, Object>> getStationaryItems() {
		String query = "SELECT * FROM stationary_management.tbl_master_item_types WHERE status=1001 AND is_stationary=true";
		return jdbcTemplate.queryForList(query);
	}

	public List<Map<String, Object>> getHouseKeepingItems() {
		String query = "SELECT * FROM stationary_management.tbl_master_item_types WHERE status=1001 AND is_stationary=false";
		return jdbcTemplate.queryForList(query);
	}

	public Object raiseRequest(SHKRaiseRequest shkRaiseRequest) {
		// Generate unique parent request ID
		long parentRequestId = Long
				.parseLong(new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()));

		// Insert into parent table
		String insertParentSql = "INSERT INTO stationary_management.tbl_parent_requests"
				+ "(parentrequestid, raisedby, raiseddate) VALUES (?, ?, NOW())";

		int parentResult = jdbcTemplate.update(insertParentSql, parentRequestId, shkRaiseRequest.getRaisedBy());

		// Validate parent insert
		if (parentResult != 1) {
			throw new RuntimeException("Failed to insert parent request");
		}

		// Insert into child table
		String insertChildSql = "INSERT INTO stationary_management.tbl_child_requests "
				+ "(typeid, parentrequestid, count, description) VALUES (?, ?, ?, ?)";

		List<Object[]> childParams = shkRaiseRequest.getItems().stream()
				.map(item -> new Object[] { item.getTypeId(), parentRequestId, item.getCount(), item.getDescription() })
				.collect(Collectors.toList());

		int[] childResults = jdbcTemplate.batchUpdate(insertChildSql, childParams);

		// Validate child inserts
		long successCount = Arrays.stream(childResults).filter(i -> i == 1).count();
		if (successCount != shkRaiseRequest.getItems().size()) {
			throw new RuntimeException("Failed to insert all child records. Expected: "
					+ shkRaiseRequest.getItems().size() + ", Actual: " + successCount);
		}

		return "Request Successfull";
	}

	public List<Map<String, Object>> parentRequestHistory(LocalDate fromDate, LocalDate toDate) {
		StringBuilder parentRequestQuery = new StringBuilder(" ");
		List<Object> params = new ArrayList<>();

		parentRequestQuery.append(" SELECT ").append(" a.parentrequestid AS RequestId, ")
				.append(" DATE_FORMAT(a.raiseddate, '%Y-%m-%d %H:%i:%s') AS RaisedDate, ")
				.append(" CONCAT(a.raisedby, '--', b.callname) AS RaisedBy, ")
				.append(" GROUP_CONCAT(DISTINCT d.name ORDER BY d.name) AS Items ")
				.append(" FROM stationary_management.tbl_parent_requests a ")
				.append(" LEFT JOIN hclhrm_prod.tbl_employee_primary b ON a.raisedby = b.employeesequenceno ")
				.append(" LEFT JOIN stationary_management.tbl_child_requests c ON a.parentrequestid = c.parentrequestid ")
				.append(" LEFT JOIN stationary_management.tbl_master_item_types d ON c.typeid = d.typeid ")
				.append(" WHERE 1=1 ");

		// Date conditions
		if (fromDate != null) {
			if (toDate != null) {
				parentRequestQuery.append("AND date(a.raiseddate) BETWEEN ? AND ? ");
				params.add(java.sql.Date.valueOf(fromDate));
				params.add(java.sql.Date.valueOf(toDate));
			} else {
				parentRequestQuery.append("AND date(a.raiseddate) >= ? ");
				params.add(java.sql.Date.valueOf(fromDate));
			}
		}
		parentRequestQuery.append(" GROUP BY a.parentrequestid ").append(" ORDER BY a.parentrequestid ASC ");

		return jdbcTemplate.queryForList(parentRequestQuery.toString(), params.toArray());
	}

	public List<Map<String, Object>> childRequestHistory(long requestId) {
		StringBuilder childRequestQuery = new StringBuilder();
		childRequestQuery.append(" SELECT b.name type,a.count count,a.description ")
				.append(" FROM stationary_management.tbl_child_requests a ")
				.append(" LEFT JOIN stationary_management.tbl_master_item_types b ON a.typeid=b.typeid ")
				.append(" WHERE parentrequestid=? ");
		return jdbcTemplate.queryForList(childRequestQuery.toString(), requestId);
	}

	public List<Long> getParentsRequests() {
		String ticketsQuery = "SELECT parentrequestid FROM stationary_management.tbl_parent_requests WHERE isnoted=FALSE";
		return jdbcTemplate.queryForList(ticketsQuery, Long.class);
	}

	public Object uploadItems(long requestId, int empId, String fileName, List<SHKItems> items) {
		String parentRequestQuery = "SELECT isnoted FROM stationary_management.tbl_parent_requests WHERE parentrequestid=? FOR UPDATE";
		boolean parentData = jdbcTemplate.queryForObject(parentRequestQuery, boolean.class, requestId);

		if (parentData) {
			throw new RequestItemsAlreadyUploadedException("Request items exists");
		}

		String insertChildSql = "INSERT INTO stationary_management.tbl_inventory"
				+ "(typeid, parentrequestid, count, available_count, description, createdby, createddate) VALUES (?, ?, ?, ?, ?, ?, ?)";

		LocalDateTime createdDate = LocalDateTime.now();

		List<Object[]> itemParams = items.stream().map(item -> new Object[] { item.getTypeId(), requestId,
				item.getCount(), item.getCount(), item.getDescription(), empId, Timestamp.valueOf(createdDate) })
				.collect(Collectors.toList());

		int[] itemResults = jdbcTemplate.batchUpdate(insertChildSql, itemParams);

		long successCount = Arrays.stream(itemResults).filter(i -> i == 1).count();
		if (successCount != items.size()) {
			throw new RuntimeException(
					"Failed to insert all items. Expected: " + items.size() + ", Actual: " + successCount);
		}

		String updateParentIsNotedQuery = "UPDATE stationary_management.tbl_parent_requests SET isnoted=true,document=? WHERE parentrequestid=?";
		int updatedRows = jdbcTemplate.update(updateParentIsNotedQuery, fileName, requestId);
		if (updatedRows != 1) {
			throw new RuntimeException("Failed to update parent request");
		}

		return "Items Upload Successfull";
	}

	public List<Map<String, Object>> parentRegistrationHistory(LocalDate fromDate, LocalDate toDate) {
		StringBuilder parentRegistrationQuery = new StringBuilder(" ");
		List<Object> params = new ArrayList<>();

		parentRegistrationQuery.append(
				" SELECT a.parentrequestid AS RegId,DATE_FORMAT(a.createddate, '%Y-%m-%d %H:%i:%s') AS RegDate,ifnull(CONCAT(a.createdby, '--', b.callname),'--') AS RegBy, ")
				.append(" GROUP_CONCAT(DISTINCT c.name ORDER BY c.name) AS Items,parent.document Doc ")
				.append(" FROM stationary_management.tbl_inventory a ")
				.append(" LEFT JOIN hclhrm_prod.tbl_employee_primary b ON a.createdby=b.employeesequenceno ")
				.append(" LEFT JOIN stationary_management.tbl_parent_requests parent ON a.parentrequestid=parent.parentrequestid ")
				.append(" LEFT JOIN stationary_management.tbl_master_item_types c ON a.typeid = c.typeid ")
				.append(" WHERE 1=1 ");

		// Date conditions
		if (fromDate != null) {
			if (toDate != null) {
				parentRegistrationQuery.append("AND date(a.createddate) BETWEEN ? AND ? ");
				params.add(java.sql.Date.valueOf(fromDate));
				params.add(java.sql.Date.valueOf(toDate));
			} else {
				parentRegistrationQuery.append("AND date(a.createddate) >= ? ");
				params.add(java.sql.Date.valueOf(fromDate));
			}
		}
		parentRegistrationQuery.append(" GROUP BY a.parentrequestid ").append(" ORDER BY a.inventory_id ASC ");

		return jdbcTemplate.queryForList(parentRegistrationQuery.toString(), params.toArray());
	}

	public List<Map<String, Object>> childRegistrationhistory(long requestId) {
		StringBuilder childRegistrationQuery = new StringBuilder();
		childRegistrationQuery
				.append(" SELECT b.name type,a.count count,a.available_count availablecount,a.description ")
				.append(" FROM stationary_management.tbl_inventory a ")
				.append(" LEFT JOIN stationary_management.tbl_master_item_types b ON a.typeid=b.typeid ")
				.append(" WHERE a.parentrequestid=? ");
		return jdbcTemplate.queryForList(childRegistrationQuery.toString(), requestId);
	}

	public List<Map<String, Object>> getInventory(String requestType) {
		StringBuilder inventoryQuery = new StringBuilder();
		inventoryQuery.append(
				" SELECT b.typeid id,b.name type,sum(a.count) totalcount,sum(a.available_count) availablecount ");
		inventoryQuery.append(" FROM stationary_management.tbl_inventory a ");
		inventoryQuery.append(" LEFT JOIN stationary_management.tbl_master_item_types b ON a.typeid = b.typeid ");
		if (requestType.equalsIgnoreCase("S")) {
			inventoryQuery.append(" WHERE b.is_stationary=true ");
		} else if (requestType.equalsIgnoreCase("H")) {
			inventoryQuery.append(" WHERE b.is_stationary=false ");
		}
		inventoryQuery.append(" GROUP BY a.typeid ");
		return jdbcTemplate.queryForList(inventoryQuery.toString());
	}

	public Map<String, Object> getEmpDetails(int empId) {
		StringBuilder empDetailsQuery = new StringBuilder();
		empDetailsQuery.append(
				" SELECT A.employeesequenceno 'EmpId',IFNULL(A.callname,'--') 'Name',IFNULL(C.NAME,'--') 'BU', ");
		empDetailsQuery.append(
				" IFNULL(D.NAME,'--') 'Designation',IFNULL(E.NAME,'--') 'Department',IFNULL(F.NAME, '--') 'WorkLocation' ");
		empDetailsQuery.append(" FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A ");
		empDetailsQuery
				.append(" LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS B ON A.EMPLOYEEID=B.EMPLOYEEID ");
		empDetailsQuery.append(" LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT C ON A.COMPANYID=C.BUSINESSUNITID ");
		empDetailsQuery.append(" LEFT JOIN HCLADM_PROD.TBL_DESIGNATION D ON D.DESIGNATIONID=B.DESIGNATIONID ");
		empDetailsQuery.append(" LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT E ON E.DEPARTMENTID=B.DEPARTMENTID ");
		empDetailsQuery.append(" LEFT JOIN HCLLCM_PROD.TBL_LOCATION F ON F.LOCATIONID=B.WORKLOCATIONID ");
		empDetailsQuery.append(" WHERE A.EMPLOYEESEQUENCENO=? AND A.STATUS IN (1001,1092,1401) ");
		return jdbcTemplate.queryForMap(empDetailsQuery.toString(), empId);
	}

	public Map<String, Object> getEmpgetContractorEmpDetailsDetails(int empId) {
		StringBuilder empDetailsQuery = new StringBuilder();
		empDetailsQuery.append(
				" SELECT employee_id EmpId,employee_name Name,Department,B.company_name Client,C.contract_name Contractor ");
		empDetailsQuery.append(" FROM test.tbl_contract_person_details A ");
		empDetailsQuery.append(" LEFT JOIN test.tbl_contract_company_details B ON A.company_id=B.company_id ");
		empDetailsQuery.append(" LEFT JOIN test.tbl_contracts C ON A.contract_id=C.contract_id ");
		empDetailsQuery.append(" WHERE A.employee_id=? AND A.status=1001");
		return jdbcTemplate.queryForMap(empDetailsQuery.toString(), empId);
	}

	public List<Map<String, Object>> getUploadsOfSameType(int type) {
		StringBuilder inventoryQuery = new StringBuilder();
		inventoryQuery.append(
				" SELECT inventory_id sno,parentrequestid regid,count totalcount,available_count availablecount ");
		inventoryQuery.append(" FROM stationary_management.tbl_inventory ");
		inventoryQuery.append(" WHERE typeid=? ");
		return jdbcTemplate.queryForList(inventoryQuery.toString(), type);
	}

	public List<Map<String, Object>> getRequestedItemTypes(long parentRequestId) {
		StringBuilder requestedItemsQuery = new StringBuilder();
		requestedItemsQuery.append(" SELECT B.typeid typeid,B.name name ");
		requestedItemsQuery.append(" FROM stationary_management.tbl_child_requests A ");
		requestedItemsQuery.append(" LEFT JOIN stationary_management.tbl_master_item_types B ON A.typeid=B.typeid ");
		requestedItemsQuery.append(" WHERE a.parentrequestid=? ");
		return jdbcTemplate.queryForList(requestedItemsQuery.toString(), parentRequestId);
	}

	public Object assign(AssignPojo request) {
		// Insert into parent table once
		String assignMainQuery = "INSERT INTO stationary_management.tbl_parent_assignments(empid,is_regular_employee,typeid,assigned_by,total_assign_count,assigned_date) VALUES(?,?,?,?,?,?)";
		KeyHolder mainKeyHolder = new GeneratedKeyHolder();
		int mainRowsAffected = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(assignMainQuery, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, request.getEmpId());
			ps.setBoolean(2, request.getIsRegular());
			ps.setInt(3, request.getType());
			ps.setInt(4, request.getAssignBy());
			ps.setInt(5, request.getData().stream().mapToInt(AssignList::getAssignCount).sum());
			ps.setObject(6, Timestamp.valueOf(LocalDateTime.now()));
			return ps;
		}, mainKeyHolder);
		if (mainRowsAffected != 1) {
			throw new RuntimeException("Failed to assign items");
		}
		int mainKey = Optional.ofNullable(mainKeyHolder.getKey()).map(Number::intValue)
				.orElseThrow(() -> new IllegalStateException("Error assigning items"));

		request.getData().stream().forEach(item -> {
			// Lock the row for update
			String query = "SELECT * FROM stationary_management.tbl_inventory WHERE inventory_id=? FOR UPDATE";
			Map<String, Object> data = jdbcTemplate.queryForMap(query, item.getInventoryId());

			// Fetch item name for message
			int availableStock = (int) data.get("available_count");
			int assignCount = item.getAssignCount();
			if (assignCount > availableStock) {
				StringBuilder typeQuery = new StringBuilder();
				typeQuery.append(" SELECT B.name ");
				typeQuery.append(" FROM stationary_management.tbl_inventory A ");
				typeQuery.append(" JOIN stationary_management.tbl_master_item_types B ON A.typeid=B.typeid ");
				typeQuery.append(" WHERE A.inventory_id=? ");
				String type = jdbcTemplate.queryForObject(typeQuery.toString(), String.class, item.getInventoryId());

				// Format the type name (first word capital, rest lower case)
				String[] words = type.trim().split("\\s+");
				type = IntStream.range(0, words.length).mapToObj(i -> {
					String word = words[i].toLowerCase();
					return i == 0 ? Character.toUpperCase(word.charAt(0)) + word.substring(1) : word;
				}).collect(Collectors.joining(" "));

				if (availableStock == 0) {
					throw new StockNotAvailableException(type + " are out of stock");
				} else {
					throw new StockNotAvailableException(
							type + " exceeds available stock. Only " + availableStock + " left");
				}
			} else {
				// Update available count
				String updateStockQuery = "UPDATE stationary_management.tbl_inventory SET available_count=? WHERE inventory_id=?";
				int rowsUpdated1 = jdbcTemplate.update(updateStockQuery, availableStock - assignCount,
						item.getInventoryId());
				if (rowsUpdated1 != 1) {
					throw new RuntimeException("Failed to assign items");
				}

				// Insert child assignment
				String childAssignQuery = "INSERT INTO stationary_management.tbl_child_assignments(inventory_id,assign_count,assign_id) VALUES(?,?,?)";
				int childRowsAffected = jdbcTemplate.update(childAssignQuery, item.getInventoryId(),
						item.getAssignCount(), mainKey);
				if (childRowsAffected != 1) {
					throw new RuntimeException("Failed to assign items");
				}
			}
		});
		return "Items Assigned";
	}

	public List<Map<String, Object>> assignHistory() {
		StringBuilder assignedHistoryQuery = new StringBuilder();
		assignedHistoryQuery.append(" SELECT A.empid AS EmpID, ");
		assignedHistoryQuery.append("   CASE ");
		assignedHistoryQuery.append("     WHEN is_regular_employee = TRUE THEN IFNULL(B.callname,'') ");
		assignedHistoryQuery.append("     WHEN is_regular_employee = FALSE THEN IFNULL(C.employee_name, '') ");
		assignedHistoryQuery.append("   END AS Name, ");
		assignedHistoryQuery.append(
				" D.name AS Item,A.totaL_assign_count AS AssignCount, DATE_FORMAT( A.assigned_date, '%Y-%m-%d %H:%i:%s') AS AssignDate, ");
		assignedHistoryQuery.append(" IFNULL(E.callname,'') AS AssignedBy, ");
		assignedHistoryQuery.append("   CASE ");
		assignedHistoryQuery.append("     WHEN is_regular_employee = TRUE THEN 'Regular' ");
		assignedHistoryQuery.append("     WHEN is_regular_employee = FALSE THEN 'Contract' ");
		assignedHistoryQuery.append("   END AS EmpType ");
		assignedHistoryQuery.append(" FROM stationary_management.tbl_parent_assignments A ");
		assignedHistoryQuery.append(
				" LEFT JOIN hclhrm_prod.tbl_employee_primary B ON B.employeesequenceno = A.empid AND A.is_regular_employee = TRUE ");
		assignedHistoryQuery.append(
				" LEFT JOIN test.tbl_contract_person_details C ON C.employee_id = A.empid AND A.is_regular_employee = FALSE ");
		assignedHistoryQuery.append(" LEFT JOIN stationary_management.tbl_master_item_types D ON D.typeid=A.typeid ");
		assignedHistoryQuery
				.append(" LEFT JOIN hclhrm_prod.tbl_employee_primary E ON E.employeesequenceno = A.assigned_by ");

		return jdbcTemplate.queryForList(assignedHistoryQuery.toString());
	}

}
