<template>
	<el-container>
		<el-aside width="150px">
			<el-col>
				<el-card shadow="hover" style="margin-top: 20px;height: 55px;">
					<el-link :underline="false" href="#card1">保单基本信息</el-link>
				</el-card>
				<el-card shadow="hover" style="margin-top: 2px;height: 55px;">
					<el-link :underline="false" href="#card2">报案基本信息</el-link>
				</el-card>
				<el-card shadow="hover" style="margin-top: 2px;height: 55px;">
					<el-link :underline="false" href="#card3">立案基本信息</el-link>
				</el-card>
				<el-card shadow="hover" style="margin-top: 2px;height: 55px;">
					<el-link :underline="false" href="#card4">理算基本信息</el-link>
				</el-card>
				<el-card shadow="hover" style="margin-top: 2px;height: 55px;" v-if="settlementMain.settlementType != 'F'">
					<el-link :underline="false" href="#card5">财产损失信息</el-link>
				</el-card>
        <el-card shadow="hover" style="margin-top: 2px;height: 55px;" v-if="settlementMain.settlementType != 'F'">
        	<el-link :underline="false" href="#card10">人伤损失信息</el-link>
        </el-card>
				<el-card shadow="hover" style="margin-top: 2px;height: 55px;" v-if="settlementMain.settlementType == 'F'">
					<el-link :underline="false" href="#card6">理赔费用信息</el-link>
				</el-card>
				<el-card shadow="hover" style="margin-top: 2px;height: 55px;">
					<el-link :underline="false" href="#card7">理算报告</el-link>
				</el-card>
				<el-card shadow="hover" style="margin-top: 2px;height: 55px;">
					<el-link :underline="false" href="#card8">收款人信息</el-link>
				</el-card>
				<el-card shadow="hover" style="margin-top: 2px;height: 55px;">
					<el-link :underline="false" href="#card9">金额汇总</el-link>
				</el-card>
				<el-card shadow="hover" style="margin-top: 2px;height: 55px;">
					<el-link :underline="false" href="#card11">审核信息</el-link>
				</el-card>
			</el-col>
		</el-aside>
		<el-main>
			<el-col :span="21">
				<el-collapse v-model="activeNames" style="border: none;">
					<el-card id="card1" shadow="hover">
						<el-collapse-item name="1">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">保单基本信息</span>
							</template>
							<BasePolicy :reportNo="reportNo"/>
						</el-collapse-item>
					</el-card>
					<el-card id="card2" shadow="hover" style="margin-top: 20px;">
						<el-collapse-item name="2">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">报案基本信息</span>
							</template>
							<BaseReport :reportNo="reportNo"/>
						</el-collapse-item>
					</el-card>
					<el-card id="card3" shadow="hover" style="margin-top: 20px;">
						<el-collapse-item name="3">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">立案基本信息</span>
							</template>
							<BaseClaim :reportNo="reportNo"/>
						</el-collapse-item>
					</el-card>
					<el-card id="card4" shadow="hover" style="margin-top: 20px;">
						<el-collapse-item name="4">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">理算基本信息</span>
							</template>
							<BaseSettlement :settlementMain="settlementMain"/>
						</el-collapse-item>
					</el-card>
					<el-card id="card5" shadow="hover" style="margin-top: 20px;" v-if="settlementMain.settlementType != 'F'">
						<el-collapse-item name="5">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">财产损失信息</span>
							</template>
							<BaseProp :lossProps="lossProps" :claimKindP="claimKindP"/>
						</el-collapse-item>
					</el-card>
          <el-card id="card10" shadow="hover" style="margin-top: 20px;" v-if="settlementMain.settlementType != 'F'">
          	<el-collapse-item name="10">
          		<template slot="title">
          			<span style="font-size: 16px;font-weight: 700;color: #303133;">人伤损失信息</span>
          		</template>
          		<BasePerson :lossPerson="lossPerson" :claimKindP="claimKindP"/>
          	</el-collapse-item>
          </el-card>
					<el-card id="card6" shadow="hover" style="margin-top: 20px;" v-if="settlementMain.settlementType == 'F'">
						<el-collapse-item name="6">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">理赔费用信息</span>
							</template>
							<BaseCharge :lossCharges="lossCharges" :claimKindF="claimKindF"/>
						</el-collapse-item>
					</el-card>
					<el-card id="card7" shadow="hover" style="margin-top: 20px;">
						<el-collapse-item name="7">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">理算报告</span>
							</template>
							<el-input
							  type="textarea"
							  :autosize="{ minRows: 10}"
							  v-model="settlementMain.content"
							  disabled
							></el-input>
						</el-collapse-item>
					</el-card>
					<el-card id="card8" shadow="hover" style="margin-top: 20px;">
						<el-collapse-item name="8">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">收款人信息</span>
							</template>
							<BasePayment :paymentList="paymentList"/>
						</el-collapse-item>
					</el-card>
					<el-card id="card9" shadow="hover" style="margin-top: 20px;">
						<el-collapse-item name="9">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">金额汇总</span>
							</template>
							<el-descriptions class="margin-top" :column="3" border :labelStyle="{'width':'150px'}" v-if="settlementMain.settlementType != 'F'">
								<el-descriptions-item>
									<template slot="label">赔款合计</template>
									{{settlementMain.sumAmt}}
								</el-descriptions-item>
								<el-descriptions-item>
									<template slot="label">扣除预赔金额</template>
									{{settlementMain.sumPreAmt}}
								</el-descriptions-item>
								<el-descriptions-item>
									<template slot="label">本次赔款金额</template>
									{{settlementMain.sumPaidAmt}}
								</el-descriptions-item>
								<el-descriptions-item>
									<template slot="label">实际理赔金额</template>
									{{settlementMain.sumRealPay}}
								</el-descriptions-item>
								<el-descriptions-item>
									<template slot="label">支付状态</template>
									<el-radio v-model="settlementMain.payStatus" label="1" disabled>已支付</el-radio>
									<el-radio v-model="settlementMain.payStatus" label="0" disabled>未支付</el-radio>
								</el-descriptions-item>
							</el-descriptions>
							<el-descriptions class="margin-top" :column="3" border :labelStyle="{'width':'150px'}" v-if="settlementMain.settlementType == 'F'">
								<el-descriptions-item>
									<template slot="label">费用合计</template>
									{{settlementMain.sumFee}}
								</el-descriptions-item>
								<el-descriptions-item>
									<template slot="label">实际理赔金额</template>
									{{settlementMain.sumRealPay}}
								</el-descriptions-item>
								<el-descriptions-item>
									<template slot="label">支付状态</template>
									<el-radio v-model="settlementMain.payStatus" label="1" disabled>已支付</el-radio>
									<el-radio v-model="settlementMain.payStatus" label="0" disabled>未支付</el-radio>
								</el-descriptions-item>
							</el-descriptions>
						</el-collapse-item>
					</el-card>
					<el-card id="card10" shadow="hover" style="margin-top: 20px;" v-if="settlementMain.settlementType != 'Y'">
						<el-collapse-item name="10">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">结案选项</span>
							</template>
							<el-descriptions class="margin-top" :column="3" border :labelStyle="{'width':'150px'}">
								<el-descriptions-item>
									<template slot="label">是否申请结案</template>
									<el-radio v-model="settlementMain.endCaseFlag" label="1" disabled>是</el-radio>
									<el-radio v-model="settlementMain.endCaseFlag" label="0" disabled>否</el-radio>
								</el-descriptions-item>
							</el-descriptions>
						</el-collapse-item>
					</el-card>
					<el-card id="card11" shadow="hover" style="margin-top: 20px; margin-bottom: 60px;">
						<el-collapse-item name="11">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">审核信息</span>
							</template>
							<BaseApprove :businessKey="businessKey" :handler="handler"/>
						</el-collapse-item>
					</el-card>
				</el-collapse>
			</el-col>
		</el-main>
	</el-container>
</template>

<script>
	import { initSettlementForUndwrt } from '@/api/api';
	import BasePolicy from '@/views/claim/BasePolicy.vue';
	import BaseReport from '@/views/claim/BaseReport.vue';
	import BaseClaim from '@/views/claim/BaseClaim.vue';
	import BaseApprove from '@/views/claim/BaseApprove.vue';
	import BaseSettlement from '@/views/undwrt/BaseSettlement.vue';
	import BaseProp from '@/views/undwrt/BaseProp.vue';
	import BaseCharge from '@/views/undwrt/BaseCharge.vue';
	import BasePayment from '@/views/undwrt/BasePayment.vue';
  import BasePerson from '@/views/undwrt/BasePerson.vue';
	export default {
		props: ['businessKey','reportNo','handler'],
		components: {
			BasePolicy,
			BaseReport,
			BaseClaim,
			BaseSettlement,
			BaseApprove,
			BaseProp,
			BaseCharge,
			BasePayment,
      BasePerson
		},
		data() {
			return {
				activeNames: ['1','3','4','5','6','7','8','9','10','11'],
				settlementMain: {},
				lossProps: [],
				lossCharges: [],
				claimKindP: [],
				claimKindF: [],
				paymentList: [],
        lossPerson: []
			}
		},
		methods: {
			getSettlementInfo() {
				let params = {
					reportNo: this.reportNo,
					businessKey: this.businessKey
				}
				initSettlementForUndwrt(params).then((res) => {
					this.settlementMain = res.data.data.settlementMain;
					this.claimKindP = res.data.data.claimKindP;
					this.claimKindF = res.data.data.claimKindF;
					this.paymentList = res.data.data.paymentList;
					if(res.data.data.lossProps) {
						this.lossProps = res.data.data.lossProps;
					}
					if(res.data.data.lossCharges) {
						this.lossCharges = res.data.data.lossCharges;
					}
          if(res.data.data.lossPersonList) {
            this.lossPerson = res.data.data.lossPersonList;
          }
				});
			}
		},
		mounted() {
			this.getSettlementInfo();
		}
	}
</script>

<style>
</style>
