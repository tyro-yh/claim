ALTER TABLE `s_common` 
MODIFY COLUMN `DataName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据名称' AFTER `DataCode`;

-- ----------------------------
-- Table structure for b_loss_person
-- ----------------------------
DROP TABLE IF EXISTS `b_loss_person`;
CREATE TABLE `b_loss_person`  (
  `Id` int(12) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `SettlementNo` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '理算书号',
  `ReportNo` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报案号',
  `ClaimNo` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '立案号',
  `SettlementType` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '理算类型（P赔款/F费用/Y预赔）',
  `PersonId` int(12) NULL DEFAULT NULL COMMENT '人伤跟踪Id',
  `PersonName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '人员名称',
  `IdentifyType` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件类型',
  `IdentifyNo` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件号码',
  `Sex` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `Age` int(3) NULL DEFAULT NULL COMMENT '年龄',
  `SumAmt` decimal(14, 2) NULL DEFAULT NULL COMMENT '合计金额',
  `CreateTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UpdateTime` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`Id`) USING BTREE,
  INDEX `idx_blossperson_reportno`(`ReportNo`) USING BTREE,
  INDEX `idx_blossperson_settlementno`(`SettlementNo`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '人伤损失表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_loss_person_fee
-- ----------------------------
DROP TABLE IF EXISTS `b_loss_person_fee`;
CREATE TABLE `b_loss_person_fee`  (
  `Id` int(12) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `SettlementNo` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '理算书号',
  `ReportNo` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报案号',
  `LossPersonId` int(12) NULL DEFAULT NULL COMMENT '人伤损失Id',
  `ClauseName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '条款名称',
  `ClauseCode` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '条款代码',
  `ItemName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '损失名称',
  `ItemCode` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '损失代码',
  `FeeTypeCode` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用名目',
  `Amount` decimal(14, 2) NULL DEFAULT NULL COMMENT '保险限额',
  `SumLoss` decimal(14, 2) NULL DEFAULT NULL COMMENT '定损金额',
  `SumLossChecked` decimal(14, 2) NULL DEFAULT NULL COMMENT '核损金额',
  `ClaimRate` decimal(5, 2) NULL DEFAULT NULL COMMENT '责任比例',
  `DeductAddRate` decimal(5, 2) NULL DEFAULT NULL COMMENT '免赔率',
  `DeductAddAmt` decimal(14, 2) NULL DEFAULT NULL COMMENT '免赔额',
  `SumAmt` decimal(14, 2) NULL DEFAULT NULL COMMENT '理算金额',
  `SumRealPay` decimal(14, 2) NULL DEFAULT NULL COMMENT '赔付金额',
  `CreateTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UpdateTime` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`Id`) USING BTREE,
  INDEX `idx_blosspersonfee_reportno`(`ReportNo`) USING BTREE,
  INDEX `idx_blosspersonfee_settlementno`(`SettlementNo`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '人伤损失明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_person_fee
-- ----------------------------
DROP TABLE IF EXISTS `b_person_fee`;
CREATE TABLE `b_person_fee`  (
  `Id` int(12) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ReportNo` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报案号',
  `PersonId` int(12) NULL DEFAULT NULL COMMENT '人伤跟踪Id',
  `FeeTypeCode` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用名目',
  `ClaimLoss` decimal(14, 2) NULL DEFAULT NULL COMMENT '索赔金额',
  `DeductionFee` decimal(14, 2) NULL DEFAULT NULL COMMENT '扣减金额',
  `EstimateLoss` decimal(14, 2) NULL DEFAULT NULL COMMENT '实际金额',
  `CreateTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UpdateTime` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`Id`) USING BTREE,
  INDEX `idx_bpersonfee_reportno`(`ReportNo`) USING BTREE,
  INDEX `idx_bpersonfee_personid`(`PersonId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '人伤跟踪费用表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_person_injured
-- ----------------------------
DROP TABLE IF EXISTS `b_person_injured`;
CREATE TABLE `b_person_injured`  (
  `Id` int(12) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ReportNo` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报案号',
  `PersonId` int(12) NULL DEFAULT NULL COMMENT '人伤跟踪Id',
  `InjuredPart` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '受伤部位',
  `DiagnosisName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '伤情诊断名称',
  `DiagnosisCode` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '伤情诊断代码',
  `Treatment` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '治疗方式',
  `TreatWay` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '治疗方案',
  `SpecificDiagnosis` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '具体诊断',
  `CreateTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UpdateTime` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`Id`) USING BTREE,
  INDEX `idx_bpersoninjured_reportno`(`ReportNo`) USING BTREE,
  INDEX `idx_bpersoninjured_personid`(`PersonId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '人伤受伤部位表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_person_main
-- ----------------------------
DROP TABLE IF EXISTS `b_person_main`;
CREATE TABLE `b_person_main`  (
  `Id` int(12) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ReportNo` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报案号',
  `PersonFlag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否标的人',
  `LossType` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '人伤类型',
  `PersonName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '人员名称',
  `IdentifyType` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件类型',
  `IdentifyNo` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件号码',
  `Phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `Sex` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `Age` int(3) NULL DEFAULT NULL COMMENT '年龄',
  `Birthday` datetime(0) NULL DEFAULT NULL COMMENT '出生日期',
  `Content` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '跟踪反馈',
  `SumAmt` decimal(14, 2) NULL DEFAULT NULL COMMENT '合计金额',
  `HandlerCode` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理人代码',
  `HandlerName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理人名称',
  `PersonSubDate` datetime(0) NULL DEFAULT NULL COMMENT '人伤提交时间',
  `UnderwriteFlag` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核状态（0初始/1审核中/2审核通过）',
  `UnderwriteDate` datetime(0) NULL DEFAULT NULL COMMENT '审核通过时间',
  `CreateTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UpdateTime` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`Id`) USING BTREE,
  INDEX `idx_bpersonmain_reportno`(`ReportNo`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '人伤跟踪主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_person_trace
-- ----------------------------
DROP TABLE IF EXISTS `b_person_trace`;
CREATE TABLE `b_person_trace`  (
  `Id` int(12) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ReportNo` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报案号',
  `PersonId` int(12) NULL DEFAULT NULL COMMENT '人伤跟踪Id',
  `TraceTime` datetime(0) NULL DEFAULT NULL COMMENT '跟踪日期',
  `BodyStatus` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目前身体状况',
  `PayWay` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缴费方式',
  `IncurredFee` decimal(14, 2) NULL DEFAULT NULL COMMENT '已发生医疗费用',
  `RequiredFee` decimal(14, 2) NULL DEFAULT NULL COMMENT '估计还需医疗费用',
  `TreamentContent` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '后续治疗内容',
  `TracePersonCode` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '跟踪人员代码',
  `TracePersonName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '跟踪人员名称',
  `CreateTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UpdateTime` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`Id`) USING BTREE,
  INDEX `idx_bpersontrace_reportno`(`ReportNo`) USING BTREE,
  INDEX `idx_bpersontrace_personid`(`PersonId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '人伤跟踪记录表' ROW_FORMAT = Dynamic;
