<template>
	<el-container>
		<el-aside width="150px">
			<el-col>
				<el-card shadow="hover" style="margin-top: 20px;">
					<el-link :underline="false" href="#card1">保单基本信息</el-link>
				</el-card>
				<el-card shadow="hover" style="margin-top: 2px;">
					<el-link :underline="false" href="#card2">报案基本信息</el-link>
				</el-card>
				<el-card shadow="hover" style="margin-top: 2px;">
					<el-link :underline="false" href="#card3">立案基本信息</el-link>
				</el-card>
				<el-card shadow="hover" style="margin-top: 2px;">
					<el-link :underline="false" href="#card4">审核信息</el-link>
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
					<el-card id="card4" shadow="hover" style="margin-top: 20px; margin-bottom: 60px;">
						<el-collapse-item name="4">
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
	import BasePolicy from '@/views/claim/BasePolicy.vue';
	import BaseReport from '@/views/claim/BaseReport.vue';
	import BaseClaim from '@/views/claim/BaseClaim.vue';
	import BaseApprove from '@/views/claim/BaseApprove.vue';
	export default {
		props: ['businessKey','reportNo'],
		components: {
			BasePolicy,
			BaseReport,
			BaseClaim,
			BaseApprove
		},
		data() {
			return {
				activeNames: ['1','3','4']
			}
		},
		methods: {
		},
		mounted() {
		}
	}
</script>

<style>
</style>
