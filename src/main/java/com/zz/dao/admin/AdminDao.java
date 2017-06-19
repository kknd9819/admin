package com.zz.dao.admin;

import java.util.List;

import com.zz.model.admin.Admin;
import com.zz.model.admin.vo.AdminRoleAuthority;
import com.zz.model.basic.dao.base.BaseDao;

/**
 * 后台管理员持久层接口
 * 
 * @Date 2014-12-29
 * @author 欧志辉
 * @version 1.0
 */
public interface AdminDao extends BaseDao<Admin, Long> {

	/**
	 * 根据用户名查找管理员
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return List<Admin>
	 */
	public List<Admin> findByUsername(String username);

	/**
	 * 根据角色编码查找管理员
	 * 
	 * @param roleCodes
	 *            角色编码
	 * @return List<Admin>
	 */
	public List<Admin> findByRoleCode(List<String> roleCodes);

	/**
	 * 根据管理ID查找管理员所有的的权限
	 * 
	 * @param adminId
	 * @return List<String> 管理员所有的权限
	 */
	public List<AdminRoleAuthority> findAuthoritys(Long adminId);

	/**
	 * 批量删除
	 * 
	 * @param admins
	 */
	public void batchDelete(List<Admin> admins);
}