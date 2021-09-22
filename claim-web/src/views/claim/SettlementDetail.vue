<template>
	<el-container>
		<el-aside width="150px">
			<el-col>
				<el-card shadow="hover" style="margin-top: 20px;">
					<el-link :underline="false" href="#card1">理算书基本信息</el-link>
				</el-card>
				<el-card shadow="hover" style="margin-top: 2px;">
					<el-link :underline="false" v-if="settlementType != 'F'" href="#card2">财产损失信息</el-link>
					<el-link :underline="false" v-if="settlementType == 'F'" href="#card2">理赔费用信息</el-link>
				</el-card>
				<el-card shadow="hover" style="margin-top: 2px;">
					<el-link :underline="false" href="#card3">理算报告</el-link>
				</el-card>
				<el-card shadow="hover" style="margin-top: 2px;">
					<el-link :underline="false" href="#card4">收款人信息</el-link>
				</el-card>
				<el-card shadow="hover" style="margin-top: 2px;">
					<el-link :underline="false" href="#card5">金额汇总</el-link>
				</el-card>
				<el-card shadow="hover" style="margin-top: 2px;" v-if="settlementType != 'Y'">
					<el-link :underline="false" href="#card6">结案选项</el-link>
				</el-card>
			</el-col>
		</el-aside>
		<el-main>
			<el-col :span="21">
				<el-form ref="form" :model="form" label-width="120px">
				<el-collapse v-model="activeNames" style="border: none;">
					<el-card id="card1" shadow="hover">
						<el-collapse-item name="1">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">理算书基本信息</span>
							</template>
							<el-row :gutter="20">
								<el-col :span="8">
									<el-form-item label="理算书号">
										<el-input v-model="settlementNo" placeholder="提交时自动生成" disabled></el-input>
									</el-form-item>
								</el-col>
								<el-col :span="8">
									<el-form-item label="报案号">
										<el-input v-model="form.reportNo" disabled></el-input>
									</el-form-item>
								</el-col>
								<el-col :span="8">
									<el-form-item label="立案号">
										<el-input v-model="form.claimNo" disabled></el-input>
									</el-form-item>
								</el-col>
							</el-row>
							<el-row :gutter="20">
								<el-col :span="8">
									<el-form-item label="理算书类型">
										<el-select v-model="form.settlementType" disabled>
											<el-option value="P" label="赔款理算"></el-option>
											<el-option value="F" label="费用理算"></el-option>
											<el-option value="Y" label="预赔理算"></el-option>
										</el-select>
									</el-form-item>
								</el-col>
								<el-col :span="8">
									<el-form-item label="业务归属机构">
										<el-tag type="success">{{form.comName}}</el-tag>
									</el-form-item>
								</el-col>
								<el-col :span="8">
									<el-form-item label="被保险人">
										<el-input v-model="form.insuranceName" disabled></el-input>
									</el-form-item>
								</el-col>
							</el-row>
							<el-row :gutter="20">
								<el-col :span="8">
									<el-form-item label="经办人">
										<el-input v-model="form.handlerName" disabled></el-input>
									</el-form-item>
								</el-col>
								<el-col :span="8">
									<el-form-item label="是否诉讼">
										<el-radio v-model="form.lawsuitFlag" label="1" :disabled="settlementStatus">是</el-radio>
										<el-radio v-model="form.lawsuitFlag" label="0" :disabled="settlementStatus">否</el-radio>
									</el-form-item>
								</el-col>
								<el-col :span="8">
									<el-form-item label="是否通融">
										<el-radio v-model="form.allowFlag" label="1" :disabled="settlementStatus">是</el-radio>
										<el-radio v-model="form.allowFlag" label="0" :disabled="settlementStatus">否</el-radio>
									</el-form-item>
								</el-col>
							</el-row>
						</el-collapse-item>
					</el-card>
					<el-card id="card2" shadow="hover" style="margin-top: 20px;" v-if="settlementType == 'F'">
						<el-collapse-item name="2">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">理赔费用信息</span>
							</template>
							<el-table :data="lossCharge" v-loading="loading" highlight-current-row style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
								<el-table-column label="条款-险别" min-width="180">
									<template slot-scope="scope">
										<el-select v-model="scope.row.clauseCode" :disabled="settlementStatus">
											<el-option
											  v-for="item in claimKindF"
											  :key="item.clauseCode"
											  :label="item.clauseName"
											  :value="item.clauseCode">
											</el-option>
										</el-select>
									</template>
								</el-table-column>
								<el-table-column label="费用类型" min-width="180">
									<template slot-scope="scope">
										<el-select v-model="scope.row.costType" v-if="scope.row.clauseCode" @change="eventItem2(scope.row)" :disabled="settlementStatus">
											<el-option value="1" label="差旅费"></el-option>
											<el-option value="2" label="律师费"></el-option>
											<el-option value="3" label="公估费"></el-option>
											<el-option value="4" label="诉讼费"></el-option>
											<el-option value="5" label="专家费"></el-option>
											<el-option value="6" label="其他"></el-option>
										</el-select>
									</template>
								</el-table-column>
								<el-table-column label="支付说明" width="200">
									<template slot-scope="scope">
										<el-input v-model="scope.row.remark" :disabled="settlementStatus"></el-input>
									</template>
								</el-table-column>
								<el-table-column label="支付对象" width="200">
									<template slot-scope="scope">
										<el-select v-model="scope.row.payObject" v-if="scope.row.costType" :disabled="settlementStatus">
											<el-option value="1" label="员工"></el-option>
											<el-option value="2" label="律师行"></el-option>
											<el-option value="3" label="公估公司"></el-option>
											<el-option value="4" label="其他"></el-option>
										</el-select>
									</template>
								</el-table-column>
								<el-table-column prop="sumLoss" label="定损金额" width="140">
								</el-table-column>
								<el-table-column label="赔付金额" width="140">
									<template slot-scope="scope">
										<el-input-number style="width: 100%;" v-model="scope.row.sumRealPay" :precision="2" :min="0" :controls="false" :disabled="settlementStatus"></el-input-number>
                    <span v-if="isNaN(scope.row.sumRealPay)" style="margin-top: 2px;font-size: 10px;color: #F56C6C;float: left;">赔付金额不能为空</span>
                  </template>
								</el-table-column>
								<el-table-column label="操作" width="100">
									<template slot-scope="scope">
										<el-button size="small" icon="el-icon-delete" type="danger" @click="delLossCharge(scope.$index)" :disabled="settlementStatus"></el-button>
									</template>
								</el-table-column>
							</el-table>
							<el-col :span="24" class="toolbar" style="background: #fff;">
								<el-button icon="el-icon-plus" size="small" type="primary" @click.native="addLossCharge" :disabled="settlementStatus"></el-button>
							</el-col>
						</el-collapse-item>
					</el-card>
					<el-card id="card2" shadow="hover" style="margin-top: 20px;" v-if="settlementType != 'F'">
						<el-collapse-item name="2">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">财产损失信息</span>
							</template>
							<el-table :data="lossProp" v-loading="loading" highlight-current-row style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
								<el-table-column label="条款-险别" min-width="180">
									<template slot-scope="scope">
										<el-select v-model="scope.row.clauseCode">
											<el-option
											  v-for="item in claimKindP"
											  :key="item.clauseCode"
											  :label="item.clauseName"
											  :value="item.clauseCode">
											</el-option>
										</el-select>
									</template>
								</el-table-column>
								<el-table-column label="损失类型" min-width="180">
									<template slot-scope="scope">
										<el-select v-model="scope.row.itemCode" v-if="scope.row.clauseCode" @change="eventItem(scope.row)">
											<el-option
											  v-for="item in claimKindP"
											  v-if="item.clauseCode == scope.row.clauseCode"
											  :key="item.itemCode"
											  :label="item.itemName"
											  :value="item.itemCode">
											</el-option>
										</el-select>
									</template>
								</el-table-column>
								<el-table-column prop="amount" label="保险责任限额" min-width="140">
								</el-table-column>
								<el-table-column prop="sumLoss" label="定损金额" width="140">
								</el-table-column>
								<el-table-column label="核损金额" width="140">
									<template slot-scope="scope">
										<el-input-number style="width: 100%;" v-model="scope.row.sumLossChecked" :precision="2" :min="0" :controls="false"></el-input-number>
                    <span v-if="isNaN(scope.row.sumLossChecked)" style="margin-top: 2px;font-size: 10px;color: #F56C6C;float: left;">核损金额不能为空</span>
                  </template>
								</el-table-column>
								<el-table-column label="责任比例(%)" width="100" style="display: inline;">
									<template slot-scope="scope">
										<el-input-number style="width: 100%;" v-model="scope.row.claimRate" :precision="2" :min="0" :max="100" :controls="false"></el-input-number>
                    <span v-if="isNaN(scope.row.claimRate)" style="margin-top: 2px;font-size: 10px;color: #F56C6C;float: left;">责任比例不能为空</span>
                  </template>
								</el-table-column>
								<el-table-column label="免赔率(%)" width="100">
									<template slot-scope="scope">
										<el-input-number style="width: 100%;" v-model="scope.row.deductAddRate" :precision="2" :min="0" :max="100" :controls="false"></el-input-number>
                    <span v-if="isNaN(scope.row.deductAddRate)" style="margin-top: 2px;font-size: 10px;color: #F56C6C;float: left;">免赔率不能为空</span>
									</template>
								</el-table-column>
								<el-table-column label="免赔额" width="140">
									<template slot-scope="scope">
										<el-input-number style="width: 100%;" v-model="scope.row.deductAddAmt" :precision="2" :min="0" :controls="false"></el-input-number>
                    <span v-if="isNaN(scope.row.deductAddAmt)" style="margin-top: 2px;font-size: 10px;color: #F56C6C;float: left;">免赔额不能为空</span>
                  </template>
								</el-table-column>
								<el-table-column prop="sumAmt" label="理算金额" width="140">
								</el-table-column>
								<el-table-column label="赔付金额" width="140">
									<template slot-scope="scope">
										<el-input-number style="width: 100%;" v-model="scope.row.sumRealPay" :precision="2" :min="0" :controls="false"></el-input-number>
                    <span v-if="isNaN(scope.row.sumRealPay)" style="margin-top: 2px;font-size: 10px;color: #F56C6C;float: left;">赔付金额不能为空</span>
                  </template>
								</el-table-column>
								<el-table-column label="操作" width="100">
									<template slot-scope="scope">
										<el-button size="small" icon="el-icon-delete" type="danger" @click="delLossProp(scope.$index)" :disabled="settlementStatus"></el-button>
									</template>
								</el-table-column>
							</el-table>
							<el-col :span="24" class="toolbar" style="background: #fff;">
								<el-button icon="el-icon-plus" size="small" type="primary" @click.native="addLossProp" :disabled="settlementStatus"></el-button>
							</el-col>
						</el-collapse-item>
					</el-card>
					<el-card id="card3" shadow="hover" style="margin-top: 20px;">
						<el-collapse-item name="3">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">理算报告</span>
							</template>
							<el-button type="primary" style="float: left;" size="small" @click="getContent" :disabled="settlementStatus">生成理算报告</el-button>
							<el-input v-loading="loading"
							  style="margin-top: 10px;"
							  type="textarea"
							  :autosize="{ minRows: 10}"
							  placeholder="请点击生成理算报告"
							  v-model="form.content"
							  disabled
							></el-input>
						</el-collapse-item>
					</el-card>
					<el-card id="card4" shadow="hover" style="margin-top: 20px;">
						<el-collapse-item name="4">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">收款人信息</span>
							</template>
							<div>
								<el-row>
									<el-button type="primary" style="float: left;" size="small" @click="showPayeeList" :disabled="settlementStatus">收款人登记</el-button>
									<el-button type="primary" style="float: left;" size="small" @click="showPaymentList" :disabled="settlementStatus">收款人匹配</el-button>
								</el-row>
								<el-row style="margin-top: 10px;">
									<el-table :data="paymentList" v-loading="pLoading2" highlight-current-row style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
										<el-table-column prop="payeeInfo.payeeKind" label="客户类型" min-width="120" :formatter="formatPayeeKind">
										</el-table-column>
										<el-table-column prop="payeeInfo.payeeType" label="单位/个人" min-width="120" :formatter="formatPayeeType">
										</el-table-column>
										<el-table-column prop="payeeInfo.payeeName" label="领款人名称" min-width="120">
										</el-table-column>
										<el-table-column prop="payeeInfo.payeePhone" label="联系方式" width="120">
										</el-table-column>
										<el-table-column prop="payeeInfo.certifyType" label="证件类型" width="120" :formatter="formatCertifyType">
										</el-table-column>
										<el-table-column prop="payeeInfo.certifyNo" label="证件号" width="160">
										</el-table-column>
										<el-table-column prop="payeeInfo.accountType" label="收款方式" width="120" :formatter="formatAccountType">
										</el-table-column>
										<el-table-column prop="payeeInfo.accountNo" label="帐号/卡号" width="160">
										</el-table-column>
										<el-table-column prop="amount" label="领款金额" width="120">
										</el-table-column>
										<el-table-column prop="remark" label="领款备注" width="160">
										</el-table-column>
									</el-table>
									<!-- <el-col :span="24" class="toolbar" style="background: #fff;">
										<el-pagination layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange2" @current-change="handleCurrentChange2" :page-size="5" :page-sizes="[5]" :total="total2" style="float:right;">
										</el-pagination>
									</el-col> -->
								</el-row>
							</div>
						</el-collapse-item>
					</el-card>
					<el-card id="card5" shadow="hover" style="margin-top: 20px;">
						<el-collapse-item name="5">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">金额汇总</span>
							</template>
							<el-row :gutter="20" v-if="settlementType != 'F'" v-loading="loading">
								<el-col :span="8">
									<el-form-item label="赔款合计">
										<el-input v-model="form.sumAmt" disabled></el-input>
									</el-form-item>
								</el-col>
								<el-col :span="8">
									<el-form-item label="扣除预赔金额">
										<el-input v-model="form.sumPreAmt" disabled></el-input>
									</el-form-item>
								</el-col>
								<el-col :span="8">
									<el-form-item label="本次赔款金额">
										<el-input v-model="form.sumPaidAmt" disabled></el-input>
									</el-form-item>
								</el-col>
							</el-row>
							<el-row :gutter="20" v-if="settlementType != 'F'" v-loading="loading">
								<el-col :span="8">
									<el-form-item label="实际理赔金额">
										<el-input v-model="form.sumRealPay" disabled></el-input>
									</el-form-item>
								</el-col>
								<el-col :span="8">
									<el-form-item label="支付状态">
										<el-radio v-model="form.payStatus" label="1" disabled>已支付</el-radio>
										<el-radio v-model="form.payStatus" label="0" disabled>未支付</el-radio>
									</el-form-item>
								</el-col>
							</el-row>
							<el-row :gutter="20" v-if="settlementType == 'F'" v-loading="loading">
								<el-col :span="8">
									<el-form-item label="费用合计">
										<el-input v-model="form.sumFee" disabled></el-input>
									</el-form-item>
								</el-col>
								<el-col :span="8">
									<el-form-item label="实际理赔金额">
										<el-input v-model="form.sumRealPay" disabled></el-input>
									</el-form-item>
								</el-col>
								<el-col :span="8">
									<el-form-item label="支付状态">
										<el-radio v-model="form.payStatus" label="1" disabled>已支付</el-radio>
										<el-radio v-model="form.payStatus" label="0" disabled>未支付</el-radio>
									</el-form-item>
								</el-col>
							</el-row>
						</el-collapse-item>
					</el-card>
					<el-card id="card6" shadow="hover" style="margin-top: 20px;" v-if="settlementType != 'Y'">
						<el-collapse-item name="6">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">结案选项</span>
							</template>
							<el-col :span="8">
								<el-form-item label="是否申请结案">
									<el-radio v-model="form.endCaseFlag" label="1" :disabled="settlementStatus">是</el-radio>
									<el-radio v-model="form.endCaseFlag" label="0" :disabled="settlementStatus">否</el-radio>
								</el-form-item>
							</el-col>
						</el-collapse-item>
					</el-card>
					<el-card id="card7" shadow="hover" style="margin-top: 20px; margin-bottom: 60px;">
						<el-collapse-item name="7">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">审核信息</span>
							</template>
							<el-input
							  style="width: 80%;"
							  type="textarea"
							  :autosize="{ minRows: 8}"
							  v-model="approveContent"
							  disabled
							></el-input>
						</el-collapse-item>
					</el-card>
				</el-collapse>
				</el-form>

				<el-dialog
				  title="收款人匹配"
				  :visible.sync="dialogPaymentList"
				  :close-on-click-modal="false"
				  width="80%"
				  center
				  :before-close="clossDialog2"
				  >
					<div style="height: 400px;">
						<el-table :data="paymentList" highlight-current-row style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
							<el-table-column label="领款人" min-width="180">
								<template slot-scope="scope">
									<el-select v-model="scope.row.payeeId" @change="selectPayee(scope.row)" style="width: 100%;">
										<el-option
										  v-for="item in payeeList"
										  :key="item.id"
										  :label="item.payeeName"
										  :value="item.id">
											<span style="float: left">{{ item.payeeName }}</span>
											<span style="float: right; color: #8492a6; font-size: 13px">{{ item.accountNo }}</span>
										</el-option>
									</el-select>
								</template>
							</el-table-column>
							<el-table-column label="领款金额" min-width="180">
								<template slot-scope="scope">
									<el-input-number style="width: 100%;" v-model="scope.row.amount" :precision="2" :min="0" :controls="false">
									</el-input-number>
								</template>
							</el-table-column>
							<el-table-column label="领款备注" min-width="180">
								<template slot-scope="scope">
									<el-input
									  type="textarea"
									  autosize
									  placeholder="请输入内容"
									  v-model="scope.row.remark">
									</el-input>
								</template>
							</el-table-column>
							<el-table-column label="操作" width="100">
								<template slot-scope="scope">
									<el-button size="small" icon="el-icon-delete" type="danger" @click="delPayment(scope.$index)"></el-button>
								</template>
							</el-table-column>
						</el-table>
						<el-col :span="24" class="toolbar" style="background: #fff;text-align: center;">
							<el-button icon="el-icon-plus" size="small" type="primary" @click.native="newPayment"></el-button>
						</el-col>
					</div>
				</el-dialog>

				<el-dialog
				  title="收款人登记"
				  :visible.sync="dialogPayeeList"
				  :close-on-click-modal="false"
				  width="80%"
				  center>
					<div style="height: 400px;">
						<el-table :data="payeeList" v-loading="pLoading" highlight-current-row style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
							<el-table-column prop="payeeKind" label="客户类型" min-width="120" :formatter="formatPayeeKind">
							</el-table-column>
							<el-table-column prop="payeeType" label="单位/个人" min-width="120" :formatter="formatPayeeType">
							</el-table-column>
							<el-table-column prop="payeeName" label="收款人名称" min-width="120">
							</el-table-column>
							<el-table-column prop="payeePhone" label="联系方式" width="120">
							</el-table-column>
							<el-table-column prop="certifyType" label="证件类型" width="120" :formatter="formatCertifyType">
							</el-table-column>
							<el-table-column prop="certifyNo" label="证件号" width="160">
							</el-table-column>
							<el-table-column prop="accountType" label="收款方式" width="120" :formatter="formatAccountType">
							</el-table-column>
							<el-table-column prop="accountNo" label="帐号/卡号" width="160">
							</el-table-column>
							<el-table-column label="操作" width="100">
								<template slot-scope="scope">
									<el-button size="small" type="primary" @click="editPayee(scope.row)">编辑</el-button>
								</template>
							</el-table-column>
						</el-table>
						<el-col :span="24" class="toolbar" style="background: #fff;">
							<el-button size="small" type="primary" @click="newPayee">新增收款人</el-button>
							<el-pagination layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-size="5" :page-sizes="[5]" :total="total" style="float:right;">
							</el-pagination>
						</el-col>
					</div>

					<el-dialog
					  title="收款人登记"
					  :visible.sync="dialogPayeeEdit"
					  :close-on-click-modal="false"
					  width="60%"
					  center
					  :before-close="clossDialog"
					  append-to-body>
						<el-form ref="form" :model="payeeInfo" label-width="120px" style="margin-left: 5%;width: 90%;">
							<el-row :gutter="20">
								<el-col :span="12">
									<el-form-item label="客户类型">
										<el-select v-model="payeeInfo.payeeKind">
											<el-option label="被保险人" value="01"></el-option>
											<el-option label="委托人" value="02"></el-option>
											<el-option label="受益人" value="03"></el-option>
											<el-option label="其他" value="04"></el-option>
										</el-select>
									</el-form-item>
								</el-col>
								<el-col :span="12">
									<el-form-item label="单位/个人">
										<el-radio v-model="payeeInfo.payeeType" label="1">个人</el-radio>
										<el-radio v-model="payeeInfo.payeeType" label="2">单位</el-radio>
									</el-form-item>
								</el-col>
							</el-row>
							<el-row :gutter="20">
								<el-col :span="12">
									<el-form-item label="收款人名称">
										<el-input v-model="payeeInfo.payeeName"></el-input>
									</el-form-item>
								</el-col>
								<el-col :span="12">
									<el-form-item label="联系方式">
										<el-input v-model="payeeInfo.payeePhone"></el-input>
									</el-form-item>
								</el-col>
							</el-row>
							<el-row :gutter="20">
								<el-col :span="12">
									<el-form-item label="证件类型">
										<el-select v-model="payeeInfo.certifyType">
											<el-option label="组织机构代码证" value="01"></el-option>
											<el-option label="身份证" value="02"></el-option>
										</el-select>
									</el-form-item>
								</el-col>
								<el-col :span="12">
									<el-form-item label="证件号">
										<el-input v-model="payeeInfo.certifyNo"></el-input>
									</el-form-item>
								</el-col>
							</el-row>
							<el-row :gutter="20">
								<el-col :span="12">
									<el-form-item label="收款方式">
										<el-select v-model="payeeInfo.accountType">
											<el-option label="银行卡" value="1"></el-option>
											<el-option label="支付宝" value="2"></el-option>
											<el-option label="微信" value="3"></el-option>
										</el-select>
									</el-form-item>
								</el-col>
								<el-col :span="12">
									<el-form-item label="帐号/卡号">
										<el-input v-model="payeeInfo.accountNo" @blur="changeAccountNo"></el-input>
									</el-form-item>
								</el-col>
							</el-row>
							<el-row :gutter="20" v-if="payeeInfo.accountType == '1'">
								<el-col :span="12">
									<el-form-item label="银行名称">
										<el-input v-model="payeeInfo.bankName" disabled></el-input>
									</el-form-item>
								</el-col>
								<el-col :span="12">
									<el-form-item label="开户行">
										<el-autocomplete
										  style="width: 100%;"
										  v-model="customBank"
										  :fetch-suggestions="queryCustom"
										  placeholder="请输入开户行"
										  @select="handleSelect"
										></el-autocomplete>
									</el-form-item>
								</el-col>
							</el-row>
							<el-row :gutter="20" v-if="payeeInfo.accountType == '1'">
								<el-col :span="12">
									<el-form-item label="联行号">
										<el-input v-model="payeeInfo.bankNo" disabled></el-input>
									</el-form-item>
								</el-col>
							</el-row>
						</el-form>
						<span slot="footer" class="dialog-footer">
							<el-button type="primary" @click="addPayee">保存</el-button>
							<el-button @click="clossDialog">返回</el-button>
						</span>
					</el-dialog>
				</el-dialog>
			</el-col>
			<el-row style="position: fixed;bottom: 10px;margin-left: 30%;">
				<el-button v-if="settlementNo" type="info" @click="handleCancel" :disabled="settlementStatus">注销</el-button>
				<el-button type="primary" @click="handleSave" :disabled="settlementStatus">保存</el-button>
				<el-button type="primary" @click="handleSubmit" :disabled="settlementStatus">保存并提交</el-button>
			</el-row>
		</el-main>
	</el-container>
</template>

<script>
	import { initSettlementInfo,initSettlementContent,checkAccountNo,initPayeeList,savePayee,
	initPaymentList,saveSettlement,submitSettlement,getApproveForSettlement,cancelSettlement,
	checkSettlementForSubmit,checkSettlementForSave } from '@/api/api';
	import { formatTimeToStr} from '@/common/js/date.js';
	export default {
		props: ['reportNo','settlementType','settlementNo'],
		data() {
			return {
				loading: false,
				pLoading: false,
				pLoading2: false,
				dialogPayeeList: false,
				dialogPayeeEdit: false,
				dialogPaymentList: false,
				settlementStatus: false,
				activeNames: ['1','2','3','4','5','6','7'],
				total: 0,
				// total2: 0,
				page: 1,
				// page2: 1,
				pageSize: 5,
				// pageSize2: 5,
				form: {},
				lossProp: [],
				lossCharge: [],
				claimKindP: [],
				claimKindF: [],
				paymentList: [],
				payeeList: [],
				payeeInfo: {
					reportNo: this.reportNo
				},
				customs: [],
				customBank: '',
				paymentRules: {
					payeeId: [
						{ required: true, message: '请选择领款人', trigger: 'change' }
					],
					amount: [
						{ required: true, message: '领款金额不能为空', trigger: 'blur' }
					]
				},
				approveContent: ''
			}
		},
		methods: {
			formatPayeeKind(row, column) {
				let payeeKind = '';
				let code = '';
				if(row.payeeInfo) {
					code = row.payeeInfo.payeeKind;
				} else {
					code = row.payeeKind;
				}
				switch(code) {
					case '01': payeeKind = '被保险人';break;
					case '02': payeeKind = '委托人';break;
					case '03': payeeKind = '受益人';break;
					case '04': payeeKind = '其他';break;
					default: payeeKind = '未知'
				}
				return payeeKind;
			},
			formatPayeeType(row, column) {
				let payeeType = '';
				let code = '';
				if(row.payeeInfo) {
					code = row.payeeInfo.payeeType;
				} else {
					code = row.payeeType;
				}
				switch(code) {
					case '1': payeeType = '个人';break;
					case '2': payeeType = '单位';break;
					default: payeeType = '未知'
				}
				return payeeType;
			},
			formatCertifyType(row, column) {
				let certifyType = '';
				let code = '';
				if(row.payeeInfo) {
					code = row.payeeInfo.certifyType;
				} else {
					code = row.certifyType;
				}
				switch(code) {
					case '01': certifyType = '组织机构代码证';break;
					case '02': certifyType = '身份证';break;
					default: certifyType = '未知'
				}
				return certifyType;
			},
			formatAccountType(row, column) {
				let accountType = '';
				let code = '';
				if(row.payeeInfo) {
					code = row.payeeInfo.accountType;
				} else {
					code = row.accountType;
				}
				switch(code) {
					case '1': accountType = '银行卡';break;
					case '2': accountType = '支付宝';break;
					case '3': accountType = '微信';break;
					default: accountType = '未知'
				}
				return accountType;
			},
			cellClass() {
				return 'text-align: center;'
			},
			headCellClass() {
				return 'background-color: #f5f7fa !important;text-align: center;'
			},
			getSettlement() {
				let params = {
					reportNo: this.reportNo,
					settlementType: this.settlementType,
					settlementNo: this.settlementNo
				};
				this.loading = true;
				initSettlementInfo(params).then((res) => {
					this.form = res.data.data.settlementMain;
					this.claimKindP = res.data.data.claimKindP;
					this.claimKindF = res.data.data.claimKindF;
					if(res.data.data.lossProps) {
						this.lossProp = res.data.data.lossProps;
					}
					if(res.data.data.lossCharges) {
						this.lossCharge = res.data.data.lossCharges;
					}
					if('0' != this.form.underwriteFlag) {
						this.settlementStatus = true;
					}
					this.getPaymentList();
					this.getApprove();
					setTimeout(() => {
						this.loading = false;
					},500);
				});
			},
			addLossProp() {
				this.lossProp.push({
					reportNo: this.reportNo,
					claimNo: this.form.claimNo,
					settlementNo: this.form.settlementNo,
					settlementType: this.settlementType,
          itemCode: this.claimKindP[0].itemCode,
          clauseCode: this.claimKindP[0].clauseCode,
          amount: this.claimKindP[0].amount,
          deductAddRate: this.claimKindP[0].deductAddRate,
          deductAddAmt: this.claimKindP[0].deductAddAmt,
          clauseName: this.claimKindP[0].clauseName,
          itemName: this.claimKindP[0].itemName,
          sumLoss: this.claimKindP[0].sumEstiPaid,
          sumLossChecked: this.claimKindP[0].sumEstiPaid,
          sumRealPay: this.claimKindP[0].sumEstiPaid,
					claimRate: 100
				})
			},
			addLossCharge() {
        let payObject = '';
        let costType = this.claimKindF[0].costType;
        if(costType == '1') {
        	payObject = '1';
        } else if(costType == '2') {
        	payObject = '2';
        } else if(costType == '3') {
        	payObject = '3';
        } else {
        	payObject = '4';
        }

				this.lossCharge.push({
					reportNo: this.reportNo,
					claimNo: this.form.claimNo,
					settlementNo: this.form.settlementNo,
					settlementType: this.settlementType,
          clauseCode: this.claimKindF[0].clauseCode,
          costType: this.claimKindF[0].costType,
          clauseName: this.claimKindF[0].clauseName,
          sumLoss: this.claimKindF[0].sumEstiFee,
          sumRealPay: this.claimKindF[0].sumEstiFee,
          payObject: payObject
				})
			},
			newPayment() {
				this.paymentList.push({
					reportNo: this.reportNo,
					settlementNo: this.form.settlementNo
				})
			},
			selectPayee(row) {
				this.payeeList.forEach(item => {
					if(item.id == row.payeeId) {
						row.payeeInfo = item;
					}
				})
			},
			eventItem(row) {
				let itemCode = row.itemCode;
				let clauseCode = row.clauseCode;
				this.claimKindP.forEach(item => {
					if(item.clauseCode == clauseCode && item.itemCode == itemCode) {
						row.amount = item.amount;
						row.deductAddRate = item.deductAddRate;
						row.deductAddAmt = item.deductAddAmt;
						row.clauseName = item.clauseName;
						row.itemName = item.itemName;
						row.sumLoss = item.sumEstiPaid;
						row.sumLossChecked = item.sumEstiPaid;
						row.sumRealPay = item.sumEstiPaid;
						return;
					}
				});
			},
			eventItem2(row) {
				let clauseCode = row.clauseCode;
				let costType = row.costType;
				row.sumLoss = 0;
				row.sumRealPay = 0;
				this.claimKindF.forEach(item => {
					if(item.clauseCode == clauseCode) {
						row.clauseName = item.clauseName;
						if(item.costType == costType) {
							row.sumLoss = item.sumEstiFee;
							row.sumRealPay = item.sumEstiFee;
							return;
						}
					}
				});
				if(costType == '1') {
					row.payObject = '1';
				} else if(costType == '2') {
					row.payObject = '2';
				} else if(costType == '3') {
					row.payObject = '3';
				} else {
					row.payObject = '4';
				}
			},
			delLossProp(index) {
				this.$confirm('是否删除该损失?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.lossProp.splice(index,1);
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});
			},
			delLossCharge(index) {
				this.$confirm('是否删除该费用?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.lossCharge.splice(index,1);
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});
			},
			delPayment(index) {
				this.$confirm('是否删除该领款人?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.paymentList.splice(index,1);
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});
			},
			getContent() {
        if(this.checkLoss()) {
          let params = {
          	settlementMain: this.form,
          	lossProps: this.lossProp,
          	lossCharges: this.lossCharge
          };
          this.loading = true;
          initSettlementContent(params).then((res) => {
          	this.form = res.data.data.settlementMain;
          	this.lossProp = res.data.data.lossProps;
          	this.lossCharge = res.data.data.lossCharges;
          	setTimeout(() => {
          		this.loading = false;
          	},500);
          })
        }
			},
			showPayeeList() {
				this.dialogPayeeList = true;
				this.getPayeeList();
			},
			showPaymentList() {
				if(!this.form.content) {
					this.$message.warning('请先生成理算报告');
				} else {
					this.dialogPaymentList = true
				}
			},
			handleSizeChange(val) {
				this.pageSize = val;
				this.getPayeeList();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.getPayeeList();
			},
			handleSizeChange2(val) {
				this.pageSize2 = val;
				this.getPaymentList();
			},
			handleCurrentChange2(val) {
				this.page2 = val;
				this.getPaymentList()();
			},
			getPayeeList() {
				this.pLoading = true;
				let params = {
					page: this.page,
					pageSize: this.pageSize,
					reportNo: this.reportNo
				};
				initPayeeList(params).then((res) => {
					this.payeeList = res.data.records;
					this.total = res.data.total;
					setTimeout(() => {
						this.pLoading = false;
					},500);
				});
			},
			getPaymentList() {
				this.pLoading2 = true;
				let params = {
					settlementNo: this.form.settlementNo
				};
				initPaymentList(params).then((res) => {
					this.paymentList = res.data.data;
					setTimeout(() => {
						this.pLoading2 = false;
					},500);
				});
			},
			clossDialog2() {
				if(this.paymentList && this.paymentList.length > 0) {
					let flag = true;
					let sumRealPay = 0;
					this.paymentList.forEach(item => {
						sumRealPay = sumRealPay + item.amount;
						if(!item.payeeId) {
							flag = false;
						}
					})
					if(flag) {
						if(isNaN(sumRealPay)) {
							this.$message.warning('领款金额不能为空');
						} else if(sumRealPay != this.form.sumRealPay) {
							this.$message.warning('领款金额不等于总金额');
						} else {
							this.dialogPaymentList = false;
						}
					} else {
						this.$message.warning('领款人不能为空');
					}
				} else {
					this.dialogPaymentList = false;
				}
			},
			editPayee(row) {
				this.payeeInfo = row;
				this.customBank = row.bankOutlets;
				this.dialogPayeeEdit = true;
			},
			newPayee() {
				this.payeeInfo = {
					reportNo: this.reportNo
				};
				this.customs = [];
				this.customBank = '';
				this.dialogPayeeEdit = true;
			},
			changeAccountNo() {
				//银行卡校验
				if(this.payeeInfo.accountType == '1') {
					let params = {
						accountNo: this.payeeInfo.accountNo
					};
					checkAccountNo(params).then((res) => {
						if(res && res.data.data) {
							this.payeeInfo.bankName = res.data.data.BankName;
							this.payeeInfo.bankCode = res.data.data.DirectBankCode;
							this.customs = res.data.data.customs;
						} else {
							this.payeeInfo.bankName = '';
							this.payeeInfo.bankCode = '';
							this.customs = [];
						}
					})
				}
			},
			queryCustom(queryString, cb) {
				let restaurants = this.customs;
				let results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;

				cb(results);
			},
			createFilter(queryString) {
				return (restaurant) => {
				  return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) > -1);
				};
			},
			handleSelect(item) {
				this.payeeInfo.bankOutlets = item.value;
				this.payeeInfo.bankNo = item.label;
				this.customBank = item.value;
			},
			addPayee() {
				savePayee(this.payeeInfo).then((res) => {
					if(res.data.status == '500') {
						this.$message.warning(res.data.msg);
					} else {
						this.$message.success('保存成功');
						this.dialogPayeeEdit = false;
						this.getPayeeList();
					}
				});
			},
			clossDialog() {
				this.dialogPayeeEdit = false;
				this.getPayeeList();
			},
			handleSave() {
        let params1 = {
        	reportNo:this.reportNo,
        	settlementType: this.settlementType,
        	settlementNo: this.form.settlementNo
        }
        checkSettlementForSave(params1).then(res => {
          if(res.data.data.status == '0') {
          	this.$message.warning(res.data.data.msg);
          } else {
            if(this.checkRules()) {
              let params = {
              	settlementMain: this.form,
              	paymentList: this.paymentList,
              	lossProps: this.lossProp,
              	lossCharges: this.lossCharge
              }
              saveSettlement(params).then((res) => {
              	if(res) {
              		this.$message.success("保存成功");
              		this.settlementNo = this.form.settlementNo;
              		this.getSettlement();
              	}
              });
            }
          }
        })
			},
			handleSubmit() {
        let params1 = {
        	reportNo:this.reportNo,
        	settlementType: this.settlementType,
        	settlementNo: this.form.settlementNo
        }
        checkSettlementForSave(params1).then(res => {
          if(res.data.data.status == '0') {
          	this.$message.warning(res.data.data.msg);
          } else {
            if(this.checkRules()) {
              let params = {
              	settlementMain: this.form,
              	paymentList: this.paymentList,
              	lossProps: this.lossProp,
              	lossCharges: this.lossCharge
              }
              checkSettlementForSubmit(params).then((res) => {
              	if(res.data.data.status == '0') {
              		this.$message.warning(res.data.data.msg);
              	} else {
              		submitSettlement(params).then((res2) => {
              			if(res2) {
              				this.$message.success("提交成功");
              				this.settlementNo = this.form.settlementNo;
              				this.getSettlement();
              			}
              		});
              	}
              });
            }
          }
        })
			},
			handleCancel() {
				this.$confirm('注销后无法还原，是否注销?', '申请注销', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					let params = {
						settlementNo: this.form.settlementNo
					}
					cancelSettlement(params).then((res) => {
						if(res) {
							this.$message.success('已注销');
							this.getSettlement();
						}
					})
				}).catch(() => {
					this.$message.info('已取消');
				});
			},
      checkLoss() {
        let flag = true;
        if(this.lossCharge && this.lossCharge.length > 0) {
          this.lossCharge.forEach(item => {
            if(!item.sumRealPay) {
              this.$message.warning("赔付金额不能为空");
              flag = false;
              return false;
            }
          });
        }
        if(this.lossProp && this.lossProp.length > 0) {
          this.lossProp.forEach(item => {
            if(isNaN(item.sumLossChecked)) {
              this.$message.warning("核损金额不能为空");
              flag = false;
              return false;
            }
            if(isNaN(item.claimRate)) {
              this.$message.warning("责任比例不能为空");
              flag = false;
              return false;
            }
            if(isNaN(item.deductAddRate)) {
              this.$message.warning("免赔率不能为空");
              flag = false;
              return false;
            }
            if(isNaN(item.deductAddAmt)) {
              this.$message.warning("免赔额不能为空");
              flag = false;
              return false;
            }
            if(isNaN(item.sumRealPay)) {
              this.$message.warning("赔付金额不能为空");
              flag = false;
              return false;
            }
          });
        }
        return flag;
      },
			checkRules() {
        let flag = true;
				let content = this.form.content;
				if(!content) {
					this.$message.warning("理算报告不能为空");
					return false;
				}
				let paymentList = this.paymentList;
				if(!paymentList || paymentList.length <= 0) {
					this.$message.warning("领款人不能为空");
					return false;
				}
				let sumRealPay = 0;
				this.paymentList.forEach(item => {
					sumRealPay = sumRealPay + item.amount;
				})
				if(sumRealPay != this.form.sumRealPay) {
					this.$message.warning('领款金额不等于总金额');
					return false;
				}
				if('F' != this.settlementType) {
					let sumPaid = 0;
					this.lossProp.forEach(item => {
						sumPaid = sumPaid + item.sumRealPay;
					})
					if(isNaN(sumPaid)) {
						this.$message.warning('财产损失赔付金额不能为空');
						return false;
					} else if(sumPaid != this.form.sumRealPay) {
						this.$message.warning('财产损失不等于总金额');
						return false;
					}
				} else {
					let sumFee = 0;
					this.lossCharge.forEach(item => {
						sumFee = sumFee + item.sumRealPay;
					})
					if(isNaN(sumFee)) {
						this.$message.warning('理赔费用赔付金额不能为空');
						return false;
					} else if(sumFee != this.form.sumRealPay) {
						this.$message.warning('理赔费用不等于总金额');
						return false;
					}
				}
        return true;
			},
			getApprove() {
				let params = {
					settlementNo: this.form.settlementNo
				}
				getApproveForSettlement(params).then((res) => {
					let approves = res.data.data;
					approves.forEach(item => {
						this.approveContent += formatTimeToStr(item.approveDate,"yyyy-MM-dd hh:mm:ss")+' '+item.approverName+'：'+item.content+"\n";
					})
				});
			}
		},
		mounted() {
			this.getSettlement();
			this.getPayeeList();
		}
	}
</script>

<style>
</style>
