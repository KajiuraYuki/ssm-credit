<template>
	<el-container>
		<!-- 头部 -->
		<el-header>
			<div>
				<img src="../../assets/logo.png" alt />
				<span>学分管理系统——教师端</span>
			</div>
			<div>{{username}}</div>
			<el-button type="info" @click="logout">退出</el-button>
		</el-header>
		<!-- 主体部分 -->
		<el-container>
			<!-- 侧边栏 -->
			<el-aside :width="isCollapse ? '65px' : '200px'">
				<div class="toggle-button" @click="toggleCollapse">|||</div>
				<el-menu :default-active="activePath" unique-opened :collapse="isCollapse" :collapse-transition="false" router
				 background-color="#333744" text-color="#fff" active-text-color="#409FFF">
					<!-- :unique-opened="true"->只允许展开一个菜单 -->
					<!-- :collapse-transition="false" -> 关闭动画 -->
					<!-- router -> 导航开启路由模式 -->
					<!-- 一级菜单  -->
					<el-submenu :index="item.id + ''" v-for="item in meunList" :key="item.id">
						<!-- 一级菜单模板区域 -->
						<template slot="title">
							<i :class="iconObj[item.id]"></i>
							<span>{{item.authName}}</span>
						</template>
						<!-- 二级菜单 -->
						<el-menu-item :index="'/teacher/'+subItem.path" v-for="subItem in item.children" :key="subItem.id" @click="saveNavState('/teacher/' + subItem.path)">
							<!-- 导航开启路由模式：
						    将index值作为导航路由 -->
							<!-- 二级菜单的模板区域 -->
							<template slot="title">
								<i class="el-icon-menu"></i>
								<span>{{ subItem.authName}}</span>
							</template>
						</el-menu-item>
					</el-submenu>
				</el-menu>
			</el-aside>
			<!-- 主体内容 -->
			<el-main>
				<router-view></router-view>
			</el-main>
		</el-container>
	</el-container>
</template>


<script>
	export default {
		name: 'TeacherHome',
		data() {
			return {
				username: '',
				activerouter: '',
				// 左侧菜单数据
				meunList: [{
						"id": 101,
						"authName": "学生管理",
						"children": [{
							"id": 1011,
							"authName": "学生信息管理",
							"path": "student"
						}],
					},
					{
						"id": 102,
						"authName": "课程管理",
						"children": [{
							"id": 1021,
							"authName": "课程信息管理",
							"path": "curriculum"
						}],
					},
					{
						"id": 103,
						"authName": "选课管理",
						"children": [{
							"id": 1031,
							"authName": "选课信息管理",
							"path": "course"
						}],
					},
					{
						"id": 104,
						"authName": "个人中心",
						"children": [{
							"id": 1041,
							"authName": "个人信息管理",
							"path": "self"
						}],
					}
				],
				iconObj: {
					'101': 'iconfont icon-xueshengguanli',
					'102': 'iconfont icon-zizhuxuanke',
					'103': 'iconfont icon-xuanke',
					'104': 'iconfont icon-gerenzhongxin'
				},
				// 默认不折叠
				isCollapse: false,
				// 被激活的导航地址
				activePath: ''
			}
		},
		created() {
			window.addEventListener('setItem', () => {
				this.activePath = window.sessionStorage.getItem('activePath')

			})

			this.username = window.sessionStorage.getItem("teachername")
		},
		methods: {
			// 退出登录
			logout() {
				// 清空token
				window.sessionStorage.clear();
				this.$router.push('/')
				this.$message({
					message: "您已成功退出登录!",
					type: 'success'
				});
			},

			// 菜单的折叠与展开
			toggleCollapse() {
				this.isCollapse = !this.isCollapse
			},
			// 保存链接的激活地址
			saveNavState(activePath) {
				// window.sessionStorage.setItem('activePath',activePath)
				this.resetSetItem('activePath', activePath)
				this.activePath = window.sessionStorage.getItem("activePath")
			}

		}
	}
</script>

<style>
	.el-container {
		height: 100%;
	}

	.el-header {
		background-color: #373f41;
		display: flex;
		justify-content: space-between;
		padding-left: 0;
		align-items: center;
		color: #fff;
		font-size: 20px;
		position: relative;
		width: 100%;
		height: 60px;
	}

	.el-header>div {
		display: flex;
		align-items: center;
	}

	.el-header>div>img {
		height: 40px;
	}

	.el-header>div>span {
		padding-left: 15px;
	}

	.el-aside {
		background-color: #333744;
		color: #eaedf1;

	}

	.el-aside>div {
		text-align: center;
	}

	.toggle-button {
		background-color: #4A5064;
		font-size: 10px;
		line-height: 24px;
		color: #fff;
		text-align: center;
		letter-spacing: 0.2em;
		cursor: pointer;
	}

	.el-menu {
		border: #333744;
	}
</style>
