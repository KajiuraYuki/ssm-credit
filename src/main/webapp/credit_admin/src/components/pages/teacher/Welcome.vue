<template>
	<el-container id="one">
		<!-- 引入导航栏和左侧菜单栏 -->
		<router-view></router-view>
		<div id="two">
			<!-- 面包屑导航区 -->
			<!-- 面包屑导航区 -->
			<el-breadcrumb separator-class="el-icon-arrow-right">
			  <el-breadcrumb-item>首页</el-breadcrumb-item>
			  <el-breadcrumb-item>Welcome</el-breadcrumb-item>
			</el-breadcrumb>
			<br/>
			<el-card>
				<div id="text">
					<h3>当前教师开设课程选课情况表</h3>				
					<ve-histogram height="380px" :extend="chartExtend" :data="NumData" :settings="Setting"></ve-histogram>			
				</div>	
			</el-card>
		</div>
	</el-container>
</template>
	
<script>
	export default {
		name:'Welcome',
		data () {
			this.chartExtend = {
			      series: {
			        barMaxWidth: 20					
			      },
				  minInterval: 1
			      
			}
			this.Setting = {
				metrics: ['选课人数'],
				dimension: ['kechengname']
			}
			
			return {
				NumData: {
					columns: ['kechengname','选课人数'],
					rows: []
				}
			}
		},
		created() {		
			// this.resetSetItem('activePath','/teacher/welcome')
			this.getData()
		},
		methods:{
			// 获取数据
			getData() {
				this.$axios.post('teacher/getselectionnum/'+window.sessionStorage.getItem("username"))
				.then(res => {					
					if (res.status !== 200) {
						return this.$message.error('获取数据失败！')
					}
					this.NumData.rows = res.data
				})
			}
		}
	}
	
</script>

<style scoped="scoped">
	#one {
		width: 100%;
		height: auto;		
	}
	#two {
		width: 100%;
		height: 100%;	
	}
	.el-card {
		height: 100%;
		width: 100%;
	}
	#text {
		text-align: center;
	}
	
</style>
