<template>
	<section>
		<el-card shadow="hover" style="line-height: 40px;">
			<BasePolicy :reportNo="reportNo" :needTitle="policyTitle"/>
		</el-card>
		<el-card shadow="hover" style="margin-top: 10px;margin-bottom: 20px;">
			<BaseReport :reportNo="reportNo" :needTitle="reportTitle"/>
		</el-card>
	</section>
</template>

<script>
	import BasePolicy from '@/views/claim/BasePolicy.vue';
	import BaseReport from '@/views/claim/BaseReport.vue';
	export default {
		props: ['reportNo'],
		components: {
			BasePolicy,
			BaseReport
		},
		mounted(){
		},
		data() {
		    return {
				policyTitle: '保单信息',
				reportTitle: '报案信息'
		    }
		},
		methods: {
		}
	}
</script>

<style>
</style>
