<template>
	<el-container>
		<el-aside width="150px">
			<el-col>
				<el-card shadow="hover" style="margin-top: 20px;">
					<el-link :underline="false" href="#card1">基本信息</el-link>
				</el-card>
				<el-card shadow="hover" style="margin-top: 2px;">
					<el-link :underline="false" href="#card2">结案信息</el-link>
				</el-card>
				<el-card shadow="hover" style="margin-top: 2px;">
					<el-link :underline="false" href="#card3">审核信息</el-link>
				</el-card>
			</el-col>
		</el-aside>
		<el-main>
			<el-col :span="21">
				<el-collapse v-model="activeNames" style="border: none;">
					<el-card id="card1" shadow="hover">
						<el-collapse-item name="1">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">基本信息</span>
							</template>
							<el-descriptions class="margin-top" :column="3" border :labelStyle="{'width':'150px'}">
								<el-descriptions-item>
									<template slot="label">报案号</template>
									{{endCaseInfo.reportNo}}
								</el-descriptions-item>
								<el-descriptions-item>
									<template slot="label">立案号</template>
									{{endCaseInfo.claimNo}}
								</el-descriptions-item>
								<el-descriptions-item>
									<template slot="label">结案号</template>
									{{endCaseInfo.caseNo}}
								</el-descriptions-item>
								<el-descriptions-item>
									<template slot="label">立案日期</template>
									{{endCaseInfo.claimTime}}
								</el-descriptions-item>
								<el-descriptions-item>
									<template slot="label">险种</template>
									<el-tag>{{endCaseInfo.insuranceName}}</el-tag>
								</el-descriptions-item>
								<el-descriptions-item>
									<template slot="label">被保险人</template>
									{{endCaseInfo.insuredName}}
								</el-descriptions-item>
							</el-descriptions>
						</el-collapse-item>
					</el-card>
					<el-card id="card2" shadow="hover" style="margin-top: 20px;">
						<el-collapse-item name="2">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">结案信息</span>
							</template>
							<el-descriptions class="margin-top" :column="3" border :labelStyle="{'width':'150px'}">
								<el-descriptions-item>
									<template slot="label">出险原因</template>
									<el-tag type="warning">{{endCaseInfo.damageCode}}</el-tag>
								</el-descriptions-item>
								<el-descriptions-item>
									<template slot="label">出险时间</template>
									{{endCaseInfo.damageTime}}
								</el-descriptions-item>
								<el-descriptions-item>
									<template slot="label">业务归属部门</template>
									<el-tag type="success">{{endCaseInfo.comName}}</el-tag>
								</el-descriptions-item>
								<el-descriptions-item :span="3">
									<template slot="label">出险地点</template>
									{{endCaseInfo.damageAddress}}
								</el-descriptions-item>
								<el-descriptions-item>
									<template slot="label">结案发起人</template>
									{{endCaseInfo.handler}}
								</el-descriptions-item>
								<el-descriptions-item>
									<template slot="label">结案日期</template>
									{{endCaseInfo.endCaseTime}}
								</el-descriptions-item>
								<el-descriptions-item>
									<template slot="label">结案员</template>
									{{endCaseInfo.endCaser}}
								</el-descriptions-item>
								<el-descriptions-item>
									<template slot="label">保额</template>
									{{endCaseInfo.amount}}
								</el-descriptions-item>
								<el-descriptions-item>
									<template slot="label">赔付金额</template>
									{{endCaseInfo.sumClaim}}
								</el-descriptions-item>
							</el-descriptions>
						</el-collapse-item>
					</el-card>
					<el-card id="card3" shadow="hover" style="margin-top: 20px; margin-bottom: 60px;">
						<el-collapse-item name="3">
							<template slot="title">
								<span style="font-size: 16px;font-weight: 700;color: #303133;">审核信息</span>
							</template>
							<BaseApprove :businessKey="businessKey"/>
						</el-collapse-item>
					</el-card>
				</el-collapse>
			</el-col>
		</el-main>
	</el-container>
</template>

<script>
	import { initEndCaseInfo } from '@/api/api';
	import BaseApprove from '@/views/claim/BaseApprove.vue';
	import { formatTimeToStr} from '@/common/js/date.js';
	export default {
		props: ['businessKey','reportNo'],
		components: {
			BaseApprove
		},
		data() {
			return {
				activeNames: ['1','2','3'],
				endCaseInfo: {}
			}
		},
		methods: {
			getEndCaseInfo() {
				let params = {
					reportNo: this.reportNo,
					businessKey: this.businessKey
				}
				initEndCaseInfo(params).then((res) => {
					this.endCaseInfo = res.data.data;
					this.endCaseInfo.claimTime = formatTimeToStr(this.endCaseInfo.claimTime,'yyyy-MM-dd hh:mm:ss');
					if(this.endCaseInfo.endCaseTime) {
						this.endCaseInfo.endCaseTime = formatTimeToStr(this.endCaseInfo.endCaseTime,'yyyy-MM-dd hh:mm:ss');
					}
					this.endCaseInfo.damageTime = formatTimeToStr(this.endCaseInfo.damageTime,'yyyy-MM-dd hh:mm:ss');
				})
			}
		},
		mounted() {
			this.getEndCaseInfo();
		}
	}
</script>

<style>
</style>
