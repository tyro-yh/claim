-- ----------------------------
-- Records of s_grade
-- ----------------------------
INSERT INTO `s_grade` VALUES (1, '系统管理岗', 'System', '1', '2021-10-14 10:11:51', '2021-10-14 10:11:55');
INSERT INTO `s_grade` VALUES (2, '报案岗', 'Report', '1', '2021-10-14 10:13:10', '2021-10-14 10:13:10');
INSERT INTO `s_grade` VALUES (3, '案件处理岗', 'CaseMain', '1', '2021-10-14 10:13:18', '2021-10-14 10:13:18');

-- ----------------------------
-- Records of s_user_grade
-- ----------------------------
INSERT INTO `s_user_grade` VALUES (1, 'HO0000', '管理员', '系统管理岗', 'System', '2021-10-25 10:53:42', '2021-10-25 10:53:42');
INSERT INTO `s_user_grade` VALUES (2, 'HO0000', '管理员', '报案岗', 'Report', '2021-10-25 10:53:42', '2021-10-25 10:53:42');
INSERT INTO `s_user_grade` VALUES (3, 'HO0000', '管理员', '案件处理岗', 'CaseMain', '2021-10-25 10:53:42', '2021-10-25 10:53:42');
INSERT INTO `s_user_grade` VALUES (4, 'SH0001', '李四', '报案岗', 'Report', '2021-10-25 10:55:52', '2021-10-25 10:55:52');
INSERT INTO `s_user_grade` VALUES (5, 'BJ0001', '王刚', '案件处理岗', 'CaseMain', '2021-10-25 11:34:32', '2021-10-25 11:34:32');
INSERT INTO `s_user_grade` VALUES (9, 'SYSTEM', '系统', '系统管理岗', 'System', '2021-10-25 14:36:28', '2021-10-25 14:36:28');

-- ----------------------------
-- Records of s_menu
-- ----------------------------
INSERT INTO `s_menu` VALUES (1, '/main', 'Main', 'el-icon-s-home', NULL, 0, '主页', '2021-10-26 10:23:08', '2021-10-26 10:23:08', NULL);
INSERT INTO `s_menu` VALUES (2, '/', 'Dict', 'el-icon-s-order', NULL, 0, '数据字典', '2021-10-26 10:23:08', '2021-10-26 10:29:26', 'System');
INSERT INTO `s_menu` VALUES (3, '/user', 'User', NULL, 2, 1, '员工中心', '2021-10-26 10:23:08', '2021-10-26 10:29:27', 'System');
INSERT INTO `s_menu` VALUES (4, '/company', 'Company', NULL, 2, 1, '组织架构', '2021-10-26 10:23:08', '2021-10-26 10:29:28', 'System');
INSERT INTO `s_menu` VALUES (5, '/common', 'Common', NULL, 2, 1, '其他配置', '2021-10-26 10:23:08', '2021-10-26 10:29:53', 'System');
INSERT INTO `s_menu` VALUES (6, '/', 'Claim', 'el-icon-s-claim', NULL, 0, '案件处理', '2021-10-26 10:23:08', '2021-10-26 10:23:08', NULL);
INSERT INTO `s_menu` VALUES (7, '/reportEdit', 'ReportEdit', NULL, 6, 1, '报案', '2021-10-26 10:23:08', '2021-10-26 10:30:50', 'Report');
INSERT INTO `s_menu` VALUES (8, '/policySearch', 'PolicySearch', NULL, 6, 1, '保单查询', '2021-10-26 10:23:08', '2021-10-26 10:23:08', NULL);
INSERT INTO `s_menu` VALUES (9, '/processManage', 'ProcessManage', 'el-icon-share', NULL, 0, '流程管理', '2021-10-26 10:23:08', '2021-10-26 11:19:13', 'System');
INSERT INTO `s_menu` VALUES (10, '/gradeManage', 'GradeManage', 'el-icon-share', NULL, 0, '岗位管理', '2021-10-26 10:23:08', '2021-10-26 11:19:15', 'System');