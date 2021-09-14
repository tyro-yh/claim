<template>
	<section>
		<el-descriptions class="margin-top" :column="3" border :labelStyle="{'width':'150px'}" v-loading="loading">
			<el-descriptions-item>
				<template slot="label">立案号</template>
				{{claimInfo.claimNo}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">立案时间</template>
				{{claimInfo.claimTime}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">结案号</template>
				{{claimInfo.caseNo}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">报案号</template>
				{{claimInfo.reportNo}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">报损金额</template>
				{{claimInfo.reportLoss}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">估损金额</template>
				{{claimInfo.sumDefloss}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">业务处理机构</template>
				<el-tag type="success">{{claimInfo.comName}}</el-tag>
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">出险原因</template>
				<el-tag type="warning">{{claimInfo.damageCode}}</el-tag>
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">出险时间</template>
				{{claimInfo.damageTime}}
			</el-descriptions-item>
			<el-descriptions-item :labelStyle="{'display':'none'}" :contentStyle="{'display': 'none'}">
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">出险经过描述</template>
				{{claimInfo.remark}}
			</el-descriptions-item>
		</el-descriptions>
		<el-descriptions class="margin-top" style="margin-top: 20px;" title="立案金额汇总" :column="3" border :labelStyle="{'width':'150px'}" v-loading="loading">
			<el-descriptions-item>
				<template slot="label">估损总赔款</template>
				{{claimInfo.sumEstiPaid}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">估损总费用</template>
				{{claimInfo.sumEstiFee}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">估损总金额</template>
				{{claimInfo.sumDefloss}}
			</el-descriptions-item>
		</el-descriptions>	
	</section>
</template>

<script>
	import { initClaimInfo } from '@/api/api';
	import { formatTimeToStr} from '@/common/js/date.js';
	export default {
		props: ['reportNo'],
		mounted(){
			this.getClaimInfo();
		},
		data() {
		    return {
				loading: false,
				claimInfo: {}
		    }
		},
		methods: {
			getClaimInfo() {
				let params = {
					reportNo: this.reportNo
				}
				this.loading = true;
				initClaimInfo(params).then((res) => {
					this.claimInfo = res.data.data.claimMain;
					this.claimInfo.claimTime = formatTimeToStr(this.claimInfo.claimTime,'yyyy-MM-dd hh:mm:ss');
					this.claimInfo.reportTime = formatTimeToStr(this.claimInfo.reportTime,'yyyy-MM-dd hh:mm:ss');
					this.claimInfo.damageTime = formatTimeToStr(this.claimInfo.damageTime,'yyyy-MM-dd hh:mm:ss');
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
