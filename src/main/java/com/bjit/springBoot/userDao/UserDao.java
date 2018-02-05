package com.bjit.springBoot.userDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bjit.springBoot.model.User;

@Repository
public class UserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public UserDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public UserDao() {

	}

	public List<User> getAllUsers() {
		String query = "SELECT * FROM `user`";

		List<User> userList = new ArrayList<User>();
		List<Map<String, Object>> userRows = jdbcTemplate.queryForList(query);
		for (Map<String, Object> row : userRows) {
			User temp = new User();
			temp.setEmail(String.valueOf(row.get("email")));
			temp.setId(Integer.parseInt(String.valueOf(row.get("id"))));
			temp.setName(String.valueOf(row.get("name")));
			temp.setPassword(String.valueOf(row.get("password")));
			temp.setUserType(String.valueOf(row.get("userType")));
			userList.add(temp);
		}
		return userList;
	}

	public User getUserById(int userId) {
		String query = "SELECT * FROM `user` WHERE `id`=?";

		User user = jdbcTemplate.queryForObject(query, new Object[] { userId }, new RowMapper<User>() {
			public User mapRow(ResultSet rs, int rowNumber) throws SQLException {
				User temp = new User();
				temp.setEmail(rs.getString("email"));
				temp.setId(rs.getInt("id"));
				temp.setName(rs.getString("name"));
				temp.setPassword(rs.getString("password"));
				temp.setUserType(rs.getString("userType"));
				return temp;
			}
		});
		if(user != null) {
			return user;
		}else {
			System.out.println("User not found.");
			return null;
		}
	}

	public User login(String email, String password) {
		String query = "SELECT * FROM `user` WHERE email = ? AND password = ?";

		try {
			User user = jdbcTemplate.queryForObject(query, new Object[] { email, password }, new RowMapper<User>() {
				public User mapRow(ResultSet rs, int rowNumber) throws SQLException {
					User temp = new User();
					temp.setEmail(rs.getString("email"));
					temp.setId(rs.getInt("id"));
					temp.setName(rs.getString("name"));
					temp.setPassword(rs.getString("password"));
					temp.setUserType(rs.getString("userType"));
					return temp;
				}

			});
			if (user != null) {
				return user;
			} else {
				return null;
			}
		} catch (EmptyResultDataAccessException e) {
			System.out.println("User doesnot exist in the database.");
			return null;
		}
	}

	public int update(User user) {
		String query = "UPDATE `user` SET `name`= ? ,`password`= ? ,`email`= ?, `userType`=? WHERE id = ?";
		int result = jdbcTemplate.update(query,
				new Object[] { user.getName(), user.getPassword(), user.getEmail(), user.getUserType(), user.getId() });
		if (result > 0) {
			System.out.println("Successfully updated.");
		} else {
			System.out.println("Failed to update.");
		}
		return result;
	}

	public boolean deleteUser(int userId) {
		String query = "DELETE FROM `user` WHERE id=?";
		int result = jdbcTemplate.update(query, userId);

		if (result > 0) {
			System.out.println("Successfully Deleted.");
			return true;
		} else {
			System.out.println("Failed to Delete.");
			return false;
		}
	}

	public int addUser(User user) {
		String query = "INSERT INTO `user` (`name`,`password`,`email`,`userType`) VALUES (?,?,?,?)";
		int result = jdbcTemplate.update(query,
				new Object[] { user.getName(), user.getPassword(), user.getEmail(), user.getUserType() });
		if (result > 0) {
			System.out.println("Successfully inserted.");
		} else {
			System.out.println("Failed to insert.");
		}
		return result;
	}

}
