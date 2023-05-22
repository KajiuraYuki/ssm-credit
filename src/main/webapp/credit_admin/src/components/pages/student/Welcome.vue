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
					<h3>选课学分数据统计图</h3>
					<div id="shuju">
						<div id="tuone">
							<font id="text1">必修已选学分:{{this.bxuefen}}</font>
							<ve-ring height="380px" :data="BiXiuData" :settings="DataSetting"></ve-ring>
						</div>
						<div id="tutwo">
							<font id="text1">选修已选学分:{{this.xxuefen}}</font>
							<ve-ring height="380px" :data="XuanXiuData" :settings="DataSetting"></ve-ring>
						</div>
					</div>					
				</div>
			</el-card>
		</div>		
	</el-container>
</template>
	
<script>
	export default {
		name:'StuWelcome',
		data () {
			this.DataSetting = {
				limitShowNum: 3
			}
			return {
				BiXiuData: {
					columns: ['kechengname','xuefen'],
					rows: []
				},
				XuanXiuData: {
					columns: ['kechengname','xuefen'],
					rows: []
				},
				xxuefen: 0,
				bxuefen: 0
			}
		},
		created() {
			// this.resetSetItem('activePath','/student/welcome')
			this.getData()
		},
		methods:{
			getData() {
				this.$axios.post('student/getxuefenbi/' + window.sessionStorage.getItem("username"))
				.then(res => {
					if (res.status !== 200) {
						return this.$message.error('获取必修课程学分数据失败！')
					}
					this.BiXiuData.rows = res.data
					for(var i=0;i<this.BiXiuData.rows.length;i++){
						this.bxuefen += this.BiXiuData.rows[i]['xuefen']
					}
				})
				this.$axios.post('student/getxuefenxuan/' + window.sessionStorage.getItem("username"))
				.then(res => {
					if (res.status !== 200) {
						return this.$message.error("获取选修课程学分数据失败！")
					}
					this.XuanXiuData.rows = res.data
					for(var i=0;i<this.XuanXiuData.rows.length;i++){
						this.xxuefen += this.XuanXiuData.rows[i]['xuefen']
					}
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
	#tuone,#tutwo{
		width: 50%;
		float: left;
	}
	#text1 {		
		font-weight: 700;
	}
</style>
