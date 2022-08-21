export const mixin = {
  methods: {
    notify (title, type) {
      this.$notify({
        title: title,
        type: type
      })
    },
    getUrl (url) {
      return this.$store.state.httpFileUrl + url
    },
    beforeAvatorUpload (file) {
      if (!(file.type === 'image/jpeg')) {
        this.$notify.error('格式错误')
        return false
      }
      if (!((file.size / 1024 / 1024) < 2)) {
        this.$notify.error('文件大小错误')
        return false
      }
      return true
    },
    handleAvatorSuccess (res, file) {
      let _this = this
      if (res.success) {
        this.$notify.success(res.data)
        // _this.pic = res.data.pic
        _this.handleCurrentChange(_this.currentPage)
      } else {
        this.$notify.error(res.errorMsg)
      }
    }
  }
}
