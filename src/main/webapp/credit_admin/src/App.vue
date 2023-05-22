<template>
  <router-view/>
</template>

<script>
	export default {
		data() {
			return {
				t: 0
			}
		},
		created() {		
			// 设置定时任务，因为token有效时间为1小时
			this.t = setInterval(this.mytest, 60 * 60 * 1000);			
		},
		methods: {
			mytest() {
				clearInterval(this.t)
				// 清空token
				window.sessionStorage.clear();
				this.$router.push('/')
				this.$message.error("您已登录超时，请重新登录!")
			}
		}
	}
</script>

<style>
	html,body {
		width: 100%;
		height: 100%;
		margin: 0;
	}
</style>
