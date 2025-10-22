package com.hetero.heteroiconnect.gstinandcontactsinfo;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GstInAndContactsRepository {

	private final JdbcTemplate jdbcTemplate;

	public GstInAndContactsRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<GstInInfoDTO> fetchAllGstinInfo() {
		String sql = "SELECT sno, gstin_number, state FROM test.tbl_hetero_gstin_info WHERE status = 1001";

		RowMapper<GstInInfoDTO> rowMapper = (rs, rowNum) -> new GstInInfoDTO(rs.getInt("sno"),
				rs.getString("gstin_number"), rs.getString("state"));

		return jdbcTemplate.query(sql, rowMapper);
	}

	public List<ContactsInfoDTO> fetchAllContacts() {
		String sql = "SELECT sno, department, name, email_id, division FROM test.tbl_hetero_contacts_info WHERE status = 1001";

		RowMapper<ContactsInfoDTO> rowMapper = (rs, rowNum) -> new ContactsInfoDTO(rs.getInt("sno"),
				rs.getString("department"), rs.getString("name"), rs.getString("email_id"), rs.getString("division"));

		return jdbcTemplate.query(sql, rowMapper);
	}
}
