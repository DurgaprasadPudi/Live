package com.hetero.heteroiconnect.spaceallocation;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SpaceAllocationRepository {

	private final JdbcTemplate jdbcTemplate;

	public SpaceAllocationRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<SpaceAllocationDTO> insertSpaceAllocations(List<SpaceAllocationDTO> list) {
		String sql = "INSERT INTO test.tbl_space_allocation ("
				+ "department_id, designation_id, joining_date, cabin_or_seat, no_of_seats, raised_by, status, created_date_time"
				+ ") VALUES (?, ?, ?, ?, ?, ?, 1001, NOW())";
		jdbcTemplate.batchUpdate(sql, list, list.size(), (ps, dto) -> {
			ps.setInt(1, dto.getDepartmentId());
			ps.setInt(2, dto.getDesignationId());
			ps.setString(3, dto.getJoiningDate());
			ps.setString(4, dto.getCabinOrSeat());
			ps.setInt(5, dto.getNoOfSeats());
			ps.setInt(6, dto.getRaisedBy());
		});
		return list;
	}

	public List<FetchSpaceAllocationDTO> fetchSpaceAllocations(Integer raisedBy) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append("a.allocation_id, a.department_id, b.name AS dept_name, ")
				.append("a.designation_id, c.name AS desg_name, a.joining_date, ")
				.append("a.cabin_or_seat, a.no_of_seats, a.status, a.comments, ")
				.append("DATE_FORMAT(a.created_date_time, '%Y-%m-%d') AS created_date_time, a.raised_by, d.callname ")
				.append("FROM test.tbl_space_allocation a ")
				.append("LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT b ON a.department_id = b.departmentid ")
				.append("LEFT JOIN HCLADM_PROD.TBL_DESIGNATION c ON a.designation_id = c.designationid ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_primary d ON a.raised_by = d.employeesequenceno ");
		if (raisedBy != null && raisedBy > 0) {
			sql.append("WHERE a.raised_by = ").append(raisedBy);
		}
		return jdbcTemplate.query(sql.toString(),
				(rs, rowNum) -> new FetchSpaceAllocationDTO(rs.getInt("allocation_id"), rs.getInt("department_id"),
						rs.getString("dept_name"), rs.getInt("designation_id"), rs.getString("desg_name"),
						rs.getString("joining_date"), rs.getString("cabin_or_seat"), rs.getInt("no_of_seats"),
						rs.getInt("status"), rs.getString("comments"), rs.getString("created_date_time"),
						rs.getInt("raised_by"), rs.getString("callname")));
	}

	public int updateStatusAndComments(int allocationId, String comments) {
		String sql = "UPDATE test.tbl_space_allocation SET status = 1002, comments = ? WHERE allocation_id = ?";
		return jdbcTemplate.update(sql, comments, allocationId);
	}
}
