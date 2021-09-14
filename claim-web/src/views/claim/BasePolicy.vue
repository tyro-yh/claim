<template>
	<section>
		<el-descriptions class="margin-top" :title="needTitle" :column="3" border :labelStyle="{'width':'150px'}" v-loading="loading">
			<template slot="extra" v-if="needTitle">
				<el-button-group>
				  <el-button type="primary" size="small" icon="el-icon-search" @click.native="policyDrawer = true">详情</el-button>
				</el-button-group>
			</template>
			<el-descriptions-item>
				<template slot="label">保单号</template>
				{{policyInfo.policyNo}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">险种</template>
				<el-tag>{{policyInfo.insuranceName}}</el-tag>
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">被保险人</template>
				{{policyInfo.insuredName}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">保险币别</template>
				人民币
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">总保额</template>
				{{policyInfo.sumAmount}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">总保费</template>
				{{policyInfo.sumPremium}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">承保机构</template>
				<el-tag type="success">{{policyInfo.comName}}</el-tag>
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">保单起期</template>
				{{policyInfo.startTime}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">保单止期</template>
				{{policyInfo.endTime}}
			</el-descriptions-item>
		</el-descriptions>
		<el-drawer
		  :visible.sync="policyDrawer"
		  direction="rtl"
		  size="50%">
			<div slot="title" class="clearfix">
				<span style="font-size: 22px;">保单信息</span>
			</div>
			<el-form ref="form" :model="policyInfo" style="width: 95%;">
				<el-form-item>
					<el-col :span="3">保单号</el-col>
					<el-col :span="9">
						<el-input v-model="policyInfo.policyNo" disabled></el-input>
					</el-col>
					<el-col :span="3">投保单号</el-col>
					<el-col :span="9">
						<el-input v-model="policyInfo.proposalNo" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">保单类型</el-col>
					<el-col :span="9">
						<el-radio v-model="policyInfo.policyType" disabled label="01">个人</el-radio>
						<el-radio v-model="policyInfo.policyType" disabled label="02">团体</el-radio>
					</el-col>
					<el-col :span="3">险种</el-col>
					<el-col :span="9">
						<el-input v-model="policyInfo.insuranceName" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">被保险人名称</el-col>
					<el-col :span="9">
						<el-input v-model="policyInfo.insuredName" disabled></el-input>
					</el-col>
					<el-col :span="3">被保险人证件</el-col>
					<el-col :span="9">
						<el-input v-model="policyInfo.insuredCode" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">归属机构</el-col>
					<el-col :span="9">
						<el-input v-model="policyInfo.comName" disabled></el-input>
					</el-col>
					<el-col :span="3">保单状态</el-col>
					<el-col :span="9">
						<el-select v-model="policyInfo.policyStatus" disabled>
							<el-option value="0" label="未生效"></el-option>
							<el-option value="1" label="已生效"></el-option>
							<el-option value="2" label="已过期"></el-option>
							<el-option value="9" label="已退保"></el-option>
						</el-select>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">投保人名称</el-col>
					<el-col :span="9">
						<el-input v-model="policyInfo.appliName" disabled></el-input>
					</el-col>
					<el-col :span="3">投保人证件</el-col>
					<el-col :span="9">
						<el-input v-model="policyInfo.appliCode" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">主被保险人名称</el-col>
					<el-col :span="9">
						<el-input v-model="policyInfo.insuredName" disabled></el-input>
					</el-col>
					<el-col :span="3">主被保险人证件</el-col>
					<el-col :span="9">
						<el-input v-model="policyInfo.insuredCode" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">总保额</el-col>
					<el-col :span="9">
						<el-input v-model="policyInfo.sumAmount" disabled></el-input>
					</el-col>
					<el-col :span="3">总保费</el-col>
					<el-col :span="9">
						<el-input v-model="policyInfo.sumPremium" disabled></el-input>
					</el-col>
				</el-form-item>
			</el-form>
		</el-drawer>
	</section>
</template>

<script>
	import { initPolicyInfo } from '@/api/api';
	import { formatTimeToStr} from '@/common/js/date.js';
	export default {
		props: ['reportNo','needTitle'],
		mounted(){
			this.getPolicyInfo();
		},
		data() {
		    return {
				policyDrawer: false,
				loading: false,
				policyInfo: {}
		    }
		},
		methods: {
			getPolicyInfo() {
				let params = {
					reportNo: this.reportNo
				}
				this.loading = true;
				initPolicyInfo(params).then((res) => {
					this.policyInfo = res.data.data;
					this.policyInfo.startTime = formatTimeToStr(this.policyInfo.startTime,'yyyy-MM-dd');
					this.policyInfo.endTime = formatTimeToStr(this.policyInfo.endTime,'yyyy-MM-dd');
					setTimeout(() => {
						this.loading = false;
					},500);
				})
			}
		}
	}
</script>

<style>
</style>
