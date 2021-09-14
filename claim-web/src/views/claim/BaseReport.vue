<template>
	<section>
		<el-descriptions class="margin-top" :title="needTitle" :column="3" border :labelStyle="{'width':'150px'}" v-loading="loading">
			<template slot="extra" v-if="needTitle">
				<el-button-group>
				  <el-button type="primary" size="small" icon="el-icon-search" @click.native="reportDrawer = true">详情</el-button>
				</el-button-group>
			</template>
			<el-descriptions-item>
				<template slot="label">报案号</template>
				{{reportInfo.reportNo}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">报案时间</template>
				{{reportInfo.reportTime}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">报损金额</template>
				{{reportInfo.reportLoss}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">出险原因</template>
				<el-tag type="warning">{{reportInfo.damageCode}}</el-tag>
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">出险时间</template>
				{{reportInfo.damageTime}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">出险地点</template>
				{{reportInfo.province+'-'+reportInfo.city+'-'+reportInfo.countyCode+'-'+reportInfo.damageAddress}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">报案人</template>
				{{reportInfo.reportorName}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">报案人电话</template>
				{{reportInfo.reportorPhone}}
			</el-descriptions-item>
			<el-descriptions-item :labelStyle="{'display':'none'}" :contentStyle="{'display': 'none'}">
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">联系人</template>
				{{reportInfo.linkerName}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">联系人电话</template>
				{{reportInfo.linkerPhone}}
			</el-descriptions-item>
			<el-descriptions-item :labelStyle="{'display':'none'}" :contentStyle="{'display': 'none'}">
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">出险经过描述</template>
				{{reportInfo.remark}}
			</el-descriptions-item>
		</el-descriptions>
		<el-drawer
		  :visible.sync="reportDrawer"
		  direction="rtl"
		  size="50%">
			<div slot="title" class="clearfix">
				<span style="font-size: 22px;">报案信息</span>
			</div>
			<el-form ref="form" :model="reportInfo" style="width: 95%;">
				<el-form-item>
					<el-col :span="3">报案号</el-col>
					<el-col :span="9">
						<el-input v-model="reportInfo.reportNo" disabled></el-input>
					</el-col>
					<el-col :span="3">报案时间</el-col>
					<el-col :span="9">
						<el-input v-model="reportInfo.reportTime" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">保单号</el-col>
					<el-col :span="9">
						<el-input v-model="reportInfo.policyNo" disabled></el-input>
					</el-col>
					<el-col :span="3">险种</el-col>
					<el-col :span="9">
						<el-input v-model="reportInfo.insuranceName" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">被保险人名称</el-col>
					<el-col :span="9">
						<el-input v-model="reportInfo.insuredName" disabled></el-input>
					</el-col>
					<el-col :span="3">被保险人证件</el-col>
					<el-col :span="9">
						<el-input v-model="reportInfo.insuredCode" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">归属机构</el-col>
					<el-col :span="9">
						<el-input v-model="reportInfo.comName" disabled></el-input>
					</el-col>
					<el-col :span="3">保单类型</el-col>
					<el-col :span="9">
						<el-radio v-model="reportInfo.policyType" disabled label="01">个人</el-radio>
						<el-radio v-model="reportInfo.policyType" disabled label="02">团体</el-radio>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">出险原因</el-col>
					<el-col :span="9" style="display: flex;">
						<el-input v-model="reportInfo.damageCode" disabled></el-input>
					</el-col>
					<el-col :span="3">出险时间</el-col>
					<el-col :span="9">
						<el-input v-model="reportInfo.damageTime" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">出险地点</el-col>
					<el-col :span="3" style="display: flex;">
						<el-input v-model="reportInfo.province" disabled></el-input>
					</el-col>
					<el-col :span="1">-</el-col>
					<el-col :span="3">
						<el-input v-model="reportInfo.city" disabled></el-input>
					</el-col>
					<el-col :span="1">-</el-col>
					<el-col :span="3">
						<el-input v-model="reportInfo.countyCode" disabled></el-input>
					</el-col>
					<el-col :span="1">-</el-col>
					<el-col :span="9">
						<el-input v-model="reportInfo.damageAddress" maxlength="100" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">报损金额</el-col>
					<el-col :span="9" style="display: flex;">
						<el-input-number v-model="reportInfo.reportLoss" :precision="2" :min="0" label="报损金额" disabled></el-input-number>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">报案人</el-col>
					<el-col :span="9">
						<el-input v-model="reportInfo.reportorName" disabled></el-input>
					</el-col>
					<el-col :span="3">报案人电话</el-col>
					<el-col :span="9">
						<el-input v-model="reportInfo.reportorPhone" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">联系人</el-col>
					<el-col :span="9">
						<el-input v-model="reportInfo.linkerName" disabled></el-input>
					</el-col>
					<el-col :span="3">联系人电话</el-col>
					<el-col :span="9">
						<el-input v-model="reportInfo.linkerPhone" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">出险经过描述</el-col>
					<el-col :span="21">
						<el-input type="textarea" v-model="reportInfo.remark" maxlength="500" show-word-limit disabled></el-input>
					</el-col>
				</el-form-item>
			</el-form>
		</el-drawer>
	</section>
</template>

<script>
	import { initReportInfo } from '@/api/api';
	import { formatTimeToStr} from '@/common/js/date.js';
	export default {
		props: ['reportNo','needTitle'],
		mounted(){
			this.getReportInfo();
		},
		data() {
		    return {
				reportDrawer: false,
				loading: false,
				reportInfo: {}
		    }
		},
		methods: {
			getReportInfo() {
				let params = {
					reportNo: this.reportNo
				}
				this.loading = true;
				initReportInfo(params).then((res) => {
					this.reportInfo = res.data.data;
					this.reportInfo.reportTime = formatTimeToStr(this.reportInfo.reportTime,'yyyy-MM-dd hh:mm:ss');
					this.reportInfo.damageTime = formatTimeToStr(this.reportInfo.damageTime,'yyyy-MM-dd hh:mm:ss');
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
