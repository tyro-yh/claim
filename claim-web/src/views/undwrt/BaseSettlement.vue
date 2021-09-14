<template>
	<section>
		<el-descriptions class="margin-top" :column="3" border :labelStyle="{'width':'150px'}">
			<el-descriptions-item>
				<template slot="label">理算书号</template>
				{{settlementMain.settlementNo}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">理算书类型</template>
				{{formatSettlementType(settlementMain.settlementType)}}
			</el-descriptions-item>
			<el-descriptions-item>
				<template slot="label">经办人</template>
				{{settlementMain.handlerName}}
			</el-descriptions-item>
		</el-descriptions>
	</section>
</template>

<script>
	export default {
		props: ['settlementMain'],
		mounted(){
			
		},
		data() {
		    return {			
		    }
		},
		methods: {
			formatSettlementType(settlementType) {
				let text = '';
				switch(settlementType) {
					case 'P': text = '赔款理算';break;
					case 'F': text = '费用理算';break;
					case 'Y': text = '预赔理算';break;
					default: text = '未知';
				}
				return text;
			},
		}
	}
</script>

<style>
</style>
