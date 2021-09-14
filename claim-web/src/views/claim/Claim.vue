<template>
	<section>
		<el-card shadow="hover" style="line-height: 40px;" v-loading="loading">
			<el-descriptions class="margin-top" title="立案基本信息"></el-descriptions>
			<el-form ref="form" :model="claimInfo">
				<el-form-item>
					<el-col :span="2">立案号</el-col>
					<el-col :span="5">
						<el-input v-model="claimInfo.claimNo" disabled placeholder="提交时自动生成"></el-input>
					</el-col>
					<el-col :offset="1" :span="2">立案时间</el-col>
					<el-col :span="5">
						<el-date-picker
						  format="yyyy-MM-dd HH:mm:ss"	
						  v-model="claimInfo.claimTime"
						  type="datetime"
						  placeholder="审核完毕自动生成"
						  disabled>
						</el-date-picker>
					</el-col>
					<el-col :offset="1" :span="2">结案号</el-col>
					<el-col :span="5">
						<el-input v-model="claimInfo.caseNo" disabled placeholder="结案时自动生成"></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="2">保单号</el-col>
					<el-col :span="5">
						<el-input v-model="claimInfo.policyNo" disabled></el-input>
					</el-col>
					<el-col :offset="1" :span="2">险种</el-col>
					<el-col :span="5">
						<el-input v-model="claimInfo.insuranceName" disabled></el-input>
					</el-col>
					<el-col :offset="1" :span="2">被保险人</el-col>
					<el-col :span="5">
						<el-input v-model="claimInfo.insuredName" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="2">报损金额</el-col>
					<el-col :span="5">
						<el-input v-model="claimInfo.reportLoss" disabled></el-input>
					</el-col>
					<el-col :offset="1" :span="2">出险时间</el-col>
					<el-col :span="5">
						<el-date-picker
						  format="yyyy-MM-dd HH"	
						  v-model="claimInfo.damageTime"
						  type="datetime"
						  disabled>
						</el-date-picker>
					</el-col>
					<el-col :offset="1" :span="2">出险原因</el-col>
					<el-col :span="5">
						<el-input v-model="claimInfo.damageCode" disabled></el-input>
					</el-col>
				</el-form-item>
				<!-- <el-form-item>
					<el-col :span="2">是否人伤</el-col>
					<el-col :span="5">
						<el-radio v-model="claimInfo.isPersonLoss" label="1">是</el-radio>
						<el-radio v-model="claimInfo.isPersonLoss" label="0">否</el-radio>
					</el-col>
					<el-col :offset="1" v-if="claimInfo.isPersonLoss == 1" :span="2">伤</el-col>
					<el-col :span="2" v-if="claimInfo.isPersonLoss == 1">
						<el-input-number v-model="claimInfo.hert" :precision="0" :min="0" label="受伤人数"></el-input-number>
					</el-col>
					<el-col :offset="1" :span="2" v-if="claimInfo.isPersonLoss == 1">亡</el-col>
					<el-col :span="2" v-if="claimInfo.isPersonLoss == 1">
						<el-input-number v-model="claimInfo.death" :precision="0" :min="0" label="死亡人数"></el-input-number>
					</el-col>
				</el-form-item> -->
				<el-form-item>
					<el-col :span="2">出险经过描述</el-col>
					<el-col :span="10">
						<el-input type="textarea" v-model="claimInfo.remark" maxlength="500" show-word-limit disabled></el-input>
					</el-col>
				</el-form-item>
			</el-form>
		</el-card>
		<el-card shadow="hover" style="line-height: 40px;margin-top: 20px;">
			<el-descriptions class="margin-top" title="估计赔款"></el-descriptions>
			<el-table :data="claimKindP" highlight-current-row v-loading="pLoading" style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
				<el-table-column label="条款-险别" min-width="120">
					<template slot-scope="scope">
						<el-select v-model="scope.row.clauseCode" :disabled="claimStatus">
							<el-option
							  v-for="item in clauseP"
							  :key="item.clauseCode"
							  :label="item.clauseName"
							  :value="item.clauseCode">
							</el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column prop="itemName" label="损失类型" min-width="120">
					<template slot-scope="scope">
						<template v-for="clause in clauseP" v-if="clause.clauseCode == scope.row.clauseCode">
							<el-select v-model="scope.row.itemCode" @change="eventItem(scope.row)" :disabled="claimStatus">
								<el-option
								  v-for="item in clause.itemList"
								  :key="item.itemCode"
								  :label="item.itemName"
								  :value="item.itemCode">
								</el-option>
							</el-select>
						</template>
					</template>
				</el-table-column>
				<el-table-column prop="insuredName" label="被保险人名称" min-width="100">
				</el-table-column>
				<el-table-column prop="insuredCode" label="被保险人证件" min-width="120">
				</el-table-column>
				<el-table-column prop="amount" label="保险责任限额" min-width="120">
				</el-table-column>
				<el-table-column prop="currency" label="币别" width="80">
				</el-table-column>
				<el-table-column label="估损金额" width="140">
					<template slot-scope="scope">
						<el-input-number style="width: 100%;" v-model="scope.row.sumEstiPaid" :precision="2" :min="0" :controls="false" :disabled="claimStatus"></el-input-number>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="small" icon="el-icon-delete" type="danger" @click="delClaimKind(scope.$index, 'P')" :disabled="claimStatus"></el-button>
					</template>
				</el-table-column>
			</el-table>
			<el-col :span="24" class="toolbar" style="background: #fff;">
				<el-button icon="el-icon-plus" size="small" type="primary" @click.native="addClaimKindP" :disabled="claimStatus"></el-button>
			</el-col>
		</el-card>
		<el-card shadow="hover" style="line-height: 40px;margin-top: 20px;">
			<el-descriptions class="margin-top" title="估计费用"></el-descriptions>
			<el-table :data="claimKindF" highlight-current-row v-loading="fLoading" style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
				<el-table-column label="条款-险别" min-width="120">
					<template slot-scope="scope">
						<el-select v-model="scope.row.clauseCode" :disabled="claimStatus">
							<el-option
							  v-for="item in clauseF"
							  :key="item.clauseCode"
							  :label="item.clauseName"
							  :value="item.clauseCode">
							</el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column label="费用类型" min-width="120">
					<template slot-scope="scope">
						<el-select v-model="scope.row.costType" :disabled="claimStatus">
							<el-option value="1" label="差旅费"></el-option>
							<el-option value="2" label="律师费"></el-option>
							<el-option value="3" label="公估费"></el-option>
							<el-option value="4" label="诉讼费"></el-option>
							<el-option value="5" label="专家费"></el-option>
							<el-option value="6" label="其他"></el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column prop="currency" label="币别" width="80">
				</el-table-column>
				<el-table-column label="估损金额" width="140">
					<template slot-scope="scope">
						<el-input-number style="width: 100%;" v-model="scope.row.sumEstiFee" :precision="2" :min="0" :controls="false" :disabled="claimStatus"></el-input-number>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="small" icon="el-icon-delete" type="danger" @click="delClaimKind(scope.$index, 'F')" :disabled="claimStatus"></el-button>
					</template>
				</el-table-column>
			</el-table>
			<el-col :span="24" class="toolbar" style="background: #fff;">
				<el-button icon="el-icon-plus" size="small" type="primary" @click.native="addClaimKindF" :disabled="claimStatus"></el-button>
			</el-col>
		</el-card>
		<el-card shadow="hover" style="line-height: 40px;margin-top: 20px;margin-bottom: 60px;">
			<el-descriptions :labelStyle="{'text-align':'center'}" :contentStyle="{'text-align':'center'}" class="margin-top" title="立案汇总信息" direction="vertical" :column="3" border>
				<template slot="extra">
					<el-button-group>
					  <el-button type="primary" size="small" @click.native="collect" :disabled="claimStatus">金额汇总</el-button>
					</el-button-group>
				</template>
				<el-descriptions-item label="估计赔款">{{claimInfo.sumEstiPaid}}</el-descriptions-item>
				<el-descriptions-item label="估计费用">{{claimInfo.sumEstiFee}}</el-descriptions-item>
				<el-descriptions-item label="估损总金额">{{claimInfo.sumDefloss}}</el-descriptions-item>
			</el-descriptions>
		</el-card>
		<el-row style="position: fixed;bottom: 10px;margin-left: 40%;">
			<el-button type="primary" @click="handleSave" :disabled="claimStatus">保存</el-button>
			<el-button type="primary" @click="handleSubmit" :disabled="claimStatus">保存并提交</el-button>
		</el-row>
	</section>
</template>

<script>
	import { initClaimInfo,saveClaim,submitClaim } from '@/api/api';
	import { formatTimeToStr} from '@/common/js/date.js';
	export default {
		props: ['reportNo'],
		mounted() {
			this.initClaim();
		},
		computed: {
		},
		data() {
		    return {
				loading: false,
				pLoading: false,
				fLoading: false,
				claimStatus: false,
				claimInfo: {},
				claimKindP: [],
				claimKindF: [],
				clauseP: [],
				clauseF: [],
		    }
		},
		methods: {
			cellClass() {
				return 'text-align: center;'
			},
			headCellClass() {
				return 'background-color: #f5f7fa !important;text-align: center;'
			},
			initClaim() {
				let params = {
					reportNo: this.reportNo
				};
				this.loading = true;
				initClaimInfo(params).then((res) => {
					this.claimInfo = res.data.data.claimMain;
					this.claimKindP = res.data.data.claimKindP;
					this.claimKindF = res.data.data.claimKindF;
					this.clauseP = res.data.data.clauseP;
					this.clauseF = res.data.data.clauseF;
					if(this.claimInfo.claimStatus == '1' || this.claimInfo.claimStatus == '2') {
						this.claimStatus = true;
					}
					setTimeout(() => {
						this.loading = false;
					},500);
				});
			},
			addClaimKindP() {
				this.claimKindP.push({
					reportNo: this.reportNo,
					feeType: 'P',
					insuredName: this.claimInfo.insuredName,
					insuredCode: this.claimInfo.insuredCode,
					currency: '人民币'
				})
			},
			addClaimKindF() {
				this.claimKindF.push({
					reportNo: this.businessKey,
					feeType: 'F',
					currency: '人民币'
				})
			},
			eventItem(row) {
				let itemCode = row.itemCode;
				let clauseCode = row.clauseCode;
				let amount;
				let deductAddRate;
				let deductAddAmt;
				let clauseName;
				let itemName;
				this.clauseP.forEach(item => {
					if(item.clauseCode == clauseCode) {
						item.itemList.forEach(item2 => {
							if(item2.itemCode == itemCode) {
								amount = item2.amount;
								deductAddRate = item2.deductAddRate;
								deductAddAmt = item2.deductAddAmt;
								clauseName = item2.clauseName;
								itemName = item2.itemName;
								return;
							}
						})
						return;
					}
				});
				row.amount = amount;
				row.deductAddRate = deductAddRate;
				row.deductAddAmt = deductAddAmt;
				row.clauseName = clauseName;
				row.itemName = itemName;
			},
			collect() {
				let sumEstiPaid = 0;
				let sumEstiFee = 0;
				this.claimKindP.forEach(item => {
					sumEstiPaid = sumEstiPaid + item.sumEstiPaid;
				});
				this.claimKindF.forEach(item => {
					sumEstiFee = sumEstiFee + item.sumEstiFee;
				});
				this.claimInfo.sumEstiPaid = sumEstiPaid;
				this.claimInfo.sumEstiFee = sumEstiFee;
				this.claimInfo.sumDefloss = sumEstiPaid + sumEstiFee;
			},
			delClaimKind(index,type) {
				this.$confirm('是否删除该条款?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					if(type == 'P') {
						this.claimKindP.splice(index,1);
					}
					if(type == 'F') {
						this.claimKindF.splice(index,1);
					}
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});          
				});
			},
			handleSave() {
				let params = {
					claimMain: this.claimInfo,
					claimKindP: this.claimKindP,
					claimKindF: this.claimKindF
				}
				saveClaim(params).then((res) => {
					if(res) {
						this.$message.success("保存成功");
						this.initClaim();
					}
				});
			},
			handleSubmit() {
				let params = {
					claimMain: this.claimInfo,
					claimKindP: this.claimKindP,
					claimKindF: this.claimKindF
				}
				submitClaim(params).then((res) => {
					if(res) {
						this.$message.success("提交成功");
						this.initClaim();
					}
				});
			}
		}
	}
</script>

<style>
</style>
