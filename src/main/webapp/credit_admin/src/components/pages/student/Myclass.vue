<template>
	<el-container id="one">
		<router-view></router-view>
		<div id="two">
			<!-- 面包屑导航区 -->
			<!-- 面包屑导航区 -->
			<el-breadcrumb separator-class="el-icon-arrow-right">
				<el-breadcrumb-item :to="{ path: '/student/welcome' }" @click.native="saveNavState('/student/welcome')">首页</el-breadcrumb-item>
				<el-breadcrumb-item>选课中心</el-breadcrumb-item>
				<el-breadcrumb-item>我的课表</el-breadcrumb-item>
			</el-breadcrumb>
			<br />
			<el-card>
				<h2 class="myclasstitle">课程表</h2>
				<el-table 
					:data="tableData" 
					:height="tableHeight" 
					border 
					ref="table" 
					:header-cell-style="{'text-align':'center'}"
					:cell-style="{'text-align':'center'}" 
					style="width: 100%">
					<el-table-column prop="classweek" label="">
						<template slot-scope="scope">
							<el-popover trigger="hover" placement="top" title="详情">
								<p>上课节次: {{ scope.row.classweek }}</p>
								<p>上课时间: {{ scope.row.classtime }}</p>
								<div slot="reference" class="name-wrapper">
									<el-tag size="medium">{{ scope.row.classweek }}</el-tag>
								</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="classdate[0]" label="周一">
						<template slot-scope="scope">
							<el-popover trigger="hover" placement="top" title="详情">
								<p>课程名称: {{ scope.row.classdate[0] }}</p>
								<p>上课教师: {{ scope.row.teachername[0] }}</p>
								<p>上课教室: {{ scope.row.classrooms[0] }}</p>
								<p>课程类别: {{ scope.row.kechengleibie[0] }}</p>
								<p>所占学分: {{ scope.row.xuefen[0] }}</p>
								<p>开始周次: {{ scope.row.startweeks[0] }}</p>
								<p>结束周次: {{ scope.row.endweeks[0] }}</p>
								<p>上课教师: {{ scope.row.teachername[0] }}</p>
								<div slot="reference" class="name-wrapper">
									<span>{{ scope.row.classdate[0] }}</span>
								</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="classdate[1]" label="周二">
						<template slot-scope="scope">
							<el-popover trigger="hover" placement="top" title="详情">
								<p>课程名称: {{ scope.row.classdate[1] }}</p>
								<p>上课教师: {{ scope.row.teachername[1] }}</p>
								<p>上课教室: {{ scope.row.classrooms[1] }}</p>
								<p>课程类别: {{ scope.row.kechengleibie[1] }}</p>
								<p>所占学分: {{ scope.row.xuefen[1] }}</p>
								<p>开始周次: {{ scope.row.startweeks[1] }}</p>
								<p>结束周次: {{ scope.row.endweeks[1] }}</p>
								<p>上课教师: {{ scope.row.teachername[1] }}</p>
								<div slot="reference" class="name-wrapper">
									<span>{{ scope.row.classdate[1] }}</span>
								</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="classdate[2]" label="周三">
						<template slot-scope="scope">
							<el-popover trigger="hover" placement="top" title="详情">
								<p>课程名称: {{ scope.row.classdate[2] }}</p>
								<p>上课教师: {{ scope.row.teachername[2] }}</p>
								<p>上课教室: {{ scope.row.classrooms[2] }}</p>
								<p>课程类别: {{ scope.row.kechengleibie[2] }}</p>
								<p>所占学分: {{ scope.row.xuefen[2] }}</p>
								<p>开始周次: {{ scope.row.startweeks[2] }}</p>
								<p>结束周次: {{ scope.row.endweeks[2] }}</p>
								<p>上课教师: {{ scope.row.teachername[2] }}</p>
								<div slot="reference" class="name-wrapper">
									<span>{{ scope.row.classdate[2] }}</span>
								</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="classdate[3]" label="周四">
						<template slot-scope="scope">
							<el-popover trigger="hover" placement="top" title="详情">
								<p>课程名称: {{ scope.row.classdate[3] }}</p>
								<p>上课教师: {{ scope.row.teachername[3] }}</p>
								<p>上课教室: {{ scope.row.classrooms[3] }}</p>
								<p>课程类别: {{ scope.row.kechengleibie[3] }}</p>
								<p>所占学分: {{ scope.row.xuefen[3] }}</p>
								<p>开始周次: {{ scope.row.startweeks[3] }}</p>
								<p>结束周次: {{ scope.row.endweeks[3] }}</p>
								<p>上课教师: {{ scope.row.teachername[3] }}</p>
								<div slot="reference" class="name-wrapper">
									<span>{{ scope.row.classdate[3] }}</span>
								</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="classdate[4]" label="周五">
						<template slot-scope="scope">
							<el-popover trigger="hover" placement="top" title="详情">
								<p>课程名称: {{ scope.row.classdate[4] }}</p>
								<p>上课教师: {{ scope.row.teachername[4] }}</p>
								<p>上课教室: {{ scope.row.classrooms[4] }}</p>
								<p>课程类别: {{ scope.row.kechengleibie[4] }}</p>
								<p>所占学分: {{ scope.row.xuefen[4] }}</p>
								<p>开始周次: {{ scope.row.startweeks[4] }}</p>
								<p>结束周次: {{ scope.row.endweeks[4] }}</p>
								<p>上课教师: {{ scope.row.teachername[4] }}</p>
								<div slot="reference" class="name-wrapper">
									<span>{{ scope.row.classdate[4] }}</span>
								</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="classdate[5]" label="周六">
						<template slot-scope="scope">
							<el-popover trigger="hover" placement="top" title="详情">
								<p>课程名称: {{ scope.row.classdate[5] }}</p>
								<p>上课教师: {{ scope.row.teachername[5] }}</p>
								<p>上课教室: {{ scope.row.classrooms[5] }}</p>
								<p>课程类别: {{ scope.row.kechengleibie[5] }}</p>
								<p>所占学分: {{ scope.row.xuefen[5] }}</p>
								<p>开始周次: {{ scope.row.startweeks[5] }}</p>
								<p>结束周次: {{ scope.row.endweeks[5] }}</p>
								<p>上课教师: {{ scope.row.teachername[5] }}</p>
								<div slot="reference" class="name-wrapper">
									<span>{{ scope.row.classdate[5] }}</span>
								</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="classdate[6]" label="周日">
						<template slot-scope="scope">
							<el-popover trigger="hover" placement="top" title="详情">
								<p>课程名称: {{ scope.row.classdate[6] }}</p>
								<p>上课教师: {{ scope.row.teachername[6] }}</p>
								<p>上课教室: {{ scope.row.classrooms[6] }}</p>
								<p>课程类别: {{ scope.row.kechengleibie[6] }}</p>
								<p>所占学分: {{ scope.row.xuefen[6] }}</p>
								<p>开始周次: {{ scope.row.startweeks[6] }}</p>
								<p>结束周次: {{ scope.row.endweeks[6] }}</p>
								<p>上课教师: {{ scope.row.teachername[6] }}</p>
								<div slot="reference" class="name-wrapper">
									<span>{{ scope.row.classdate[6] }}</span>
								</div>
							</el-popover>
						</template>
					</el-table-column>
				</el-table>
			</el-card>
		</div>
	</el-container>
</template>

<script>
	export default {
		name: 'Myclass',
		
		data() {
			return {
				tableHeight: 100,

				tableData: [{
					classweek: '1-2',
					classtime: '08:30-10:05',
					classdate: [],
					classrooms: [],
					startweeks: [],
					endweeks: [],
					kechengleibie: [],
					teachername: [],
					xuefen: []
				}, {
					classweek: '3-4',
					classtime: '10:25-12:00',
					classdate: [],
					classrooms: [],
					startweeks: [],
					endweeks: [],
					kechengleibie: [],
					teachername: [],
					xuefen: []
				}, {
					classweek: '5-6',
					classtime: '14:00-15:35',
					classdate: [],
					classrooms: [],
					startweeks: [],
					endweeks: [],
					kechengleibie: [],
					teachername: [],
					xuefen: []
				}, {
					classweek: '7-8',
					classtime: '15:55-17:30',
					classdate: [],
					classrooms: [],
					startweeks: [],
					endweeks: [],
					kechengleibie: [],
					teachername: [],
					xuefen: []
				}, {
					classweek: '9-10',
					classtime: '18:30-20:05',
					classdate: [],
					classrooms: [],
					startweeks: [],
					endweeks: [],
					kechengleibie: [],
					teachername: [],
					xuefen: []
				}]
			}
		},
		created() {
			this.saveNavState('/student/myclass')
			this.getKechengbiao()
		},
		mounted: function() {
			this.$nextTick(function() {
				this.tableHeight = window.innerHeight - this.$refs.table.$el.offsetTop - 60;
				let self = this;
				window.onresize = function() {
					self.tableHeight = window.innerHeight - self.$refs.table.$el.offsetTop - 60;
				}
			})
		},
		methods: {
			// 保存链接的激活地址
			saveNavState(activePath) {
				this.resetSetItem('activePath', activePath)
				// window.sessionStorage.setItem('activePath',activePath)					
			},
			getKechengbiao() {
				this.$axios.get('/student/getkechengbiao/' + window.sessionStorage.getItem("username"))
					.then(res => {
						if (res.status !== 200) {
							return this.$message.error("与服务器失去联系！请联系管理员!")
						}
						console.log(res.data)
						// 循环遍历,将所有数据放入到对应的数据中,便于数据绑定
						for (let i = 0; i < res.data.length; i++) {
							let j = -1;
							let k = -1;
							switch (res.data[i].classsection) {
								case '1-2':
									j = 0;
									break;
								case '3-4':
									j = 1;
									break;
								case '5-6':
									j = 2;
									break;
								case '7-8':
									j = 3;
									break;
								case '9-10':
									j = 4;
									break;
							}
							switch (res.data[i].classweek) {
								case '周一':
									k = 0;
									break;
								case '周二':
									k = 1;
									break;
								case '周三':
									k = 2;
									break;
								case '周四':
									k = 3;
									break;
								case '周五':
									k = 4;
									break;
								case '周六':
									k = 5;
									break;
								case '周日':
									k = 6;
									break;
							}
							
							// 1.当你利用索引直接设置一个项时，例如：vm.items[indexOfItem] = newValue
							// 必须用全局方法 Vue.set(vm.items, indexOfItem, newValue) 或 实例方法 vm.$set(vm.items, indexOfItem, newValue) 来触发状态更新。
							// 2.当你修改数组的长度时，例如：vm.items.length = newLength
							// 可以使用 vm.items.splice(newLength) 
							
							this.tableData[j].classdate[k] = res.data[i].kechengname
							this.tableData[j].classrooms[k] = res.data[i].classroom
							this.tableData[j].startweeks[k] = res.data[i].startweek
							this.tableData[j].endweeks[k] = res.data[i].endweek
							this.tableData[j].kechengleibie[k] = res.data[i].kechengleibie
							this.tableData[j].teachername[k] = res.data[i].name
							this.tableData[j].xuefen[k] = res.data[i].xuefen
							// 强制刷新
							this.$forceUpdate()
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
		padding-top: 1%;
	}

	#two {
		width: 100%;
		height: 100%;
	}

	.el-card {
		height: 100%;
		width: 100%;
	}

	.el-table {
		padding-top: 20px;
	}

	.el-pagination {
		text-align: left;
		padding-top: 20px;
	}

	.myclasstitle {
		text-align: center;
	}
</style>
