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
					<h3>各个学院教师及学生数据统计图</h3>				
					<ve-histogram height="380px" :extend="chartExtend" :data="NumsData" :settings="Setting"></ve-histogram>			
				</div>				
			</el-card>
		</div>		
	</el-container>
</template>

<script>	
	export default {
		name: 'Welcome',
		data () {
			this.chartExtend = {				
			      series: {
			        barMaxWidth: 20					
			      },
				  minInterval: 1
			      
			}
			this.Setting = {
				metrics: ['学生人数','教师人数'],
				dimension: ['collegename']
			}
			
			
			return{
				NumsData: {
					columns: ['collegename','学生人数','教师人数'],
					rows:[]
				},
				// stunum: 0,
				// teanum: 0
				
			}
							
		},
		created() {
			// this.resetSetItem('activePath','/admin/welcome')
			this.getData()
		},
		methods: {
			// 获取数据
			getData() {
				// 获取学生和教师数据
				this.$axios.get('admin/getnums').then(res => {					
					if (res.status !== 200) {
						return this.$message.error("获取学生数据失败！")
					}
					this.NumsData.rows = res.data					
					// for(var i=0;i<this.NumsData.rows.length;i++) {
					// 	this.stunum += this.NumsData.rows[i]['学生人数'];	
					// 	this.teanum += this.NumsData.rows[i]['教师人数']
					// }					
				})
				
			},
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
