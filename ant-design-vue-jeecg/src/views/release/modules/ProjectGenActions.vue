<template>
  <div>
    <!-- <template slot="action"> -->
      <!-- <a-button type="dashed" icon="reload" @click="reloadData"></a-button> -->
      <!-- <a-button type="primary" style="margin:0 4px" icon="cloud-upload" @click="publish">发布</a-button> -->
      <a-button-group>
        <a-button @click="handleReleaseNote">ReleaseNote</a-button>
        <a-button @click="handleReleaseMail">ReleaseMail</a-button>
        <a-button @click="handlePackagePR">产品包 PR 文本</a-button>
        <a-button @click="handleHandbookChPR">手册中文 PR 文本</a-button>
        <a-button @click="handleHandbookEnPR">手册英文 PR 文本</a-button>
        <a-button @click="handleWebsite">官网文本</a-button>
      </a-button-group>
    <!-- </template> -->
    <gen-text-drawer ref="modalForm" @ok="modalFormOk"></gen-text-drawer>
    </div>
</template>

<script>
import { getAction } from '@api/manage'
import GenTextDrawer from "@views/release/modules/GenTextDrawer.vue";
export default {
  name: 'ProjectGenActions',
  components: {
    GenTextDrawer
  },
  props: {
    publishlistId: {
      type: String,
      default: '',
      required: true
    },
  },
  data() {
    return {
      title: '',
      isMarkDown: true,
      url: {
        genReleaseNote: '/release/generateReleaseNoteContent',
        genReleaseEmail: '/release/generateReleaseMailContent',
        genPackagePr: '/release/generateProductPackagePRContent',
        genHandbookChPr: '/release/generateProductHandbookPRChContent',
        genHandbookEnPr: '/release/generateProductHandbookPREnContent',
        genWebsite: '/release/generateWebsiteContent',
      }
    }
  },
  methods: {
    modalFormOk() { },
    handleReleaseNote() {
        console.log('handleReleaseNote');
      this.isMarkDown = true
      const params = { id: this.publishlistId }
      getAction(this.url.genReleaseNote, params).then(res => {
        if (res.success) {
          console.log(res.result)
          this.txtContent = res.result
          this.$refs.modalForm.view({
            publishlistId: this.publishlistId,
            title: 'ReleaseNote',
            content: res.result,
            isMarkDown: this.isMarkDown,
            templateType: 'ReleaseNote'
          })
        } else {
          this.$message.error(res.message)
        }
      })
    },
    handleReleaseMail() {
      this.isMarkDown = false
      const params = { id: this.publishlistId }
      getAction(this.url.genReleaseEmail, params).then(res => {
        if (res.success) {
          console.log(res.result)
          this.txtContent = res.result
          this.$refs.modalForm.view({
            publishlistId: this.publishlistId,
            title: 'Release Email',
            content: res.result,
            isMarkDown: this.isMarkDown,
            templateType: 'ReleaseMail'
          })
        } else {
          this.$message.error(res.message)
        }
      })
    },
    handlePackagePR() {
      this.isMarkDown = true
      const params = { id: this.publishlistId }
      getAction(this.url.genPackagePr, params).then(res => {
        if (res.success) {
          console.log(res.result)
          this.txtContent = res.result
          this.$refs.modalForm.view({
            publishlistId: this.publishlistId,
            title: '打包 PR 生成',
            content: res.result,
            isMarkDown: this.isMarkDown,
            templateType: 'ProductPackagePRContent'
          })
        } else {
          this.$message.error(res.message)
        }
      })
    },
    handleHandbookChPR() {
      this.isMarkDown = true
      const params = { id: this.publishlistId }
      getAction(this.url.genHandbookChPr, params).then(res => {
        if (res.success) {
          console.log(res.result)
          this.txtContent = res.result
          this.$refs.modalForm.view({
            publishlistId: this.publishlistId,
            title: '手册中文 PR 生成',
            content: res.result,
            isMarkDown: this.isMarkDown,
            templateType: 'HandBookPRChContent'
          })
        } else {
          this.$message.error(res.message)
        }
      })
    },
    handleHandbookEnPR() {
      this.isMarkDown = true
      const params = { id: this.publishlistId }
      getAction(this.url.genHandbookEnPr, params).then(res => {
        if (res.success) {
          console.log(res.result)
          this.txtContent = res.result
          this.$refs.modalForm.view({
            publishlistId: this.publishlistId,
            title: '手册英文 PR 生成',
            content: res.result,
            isMarkDown: this.isMarkDown,
            templateType: 'HandBookPREnContent'
          })
        } else {
          this.$message.error(res.message)
        }
      })
    },
    handleWebsite() {
      this.isMarkDown = true
      const params = { id: this.publishlistId }
      getAction(this.url.genWebsite, params).then(res => {
        if (res.success) {
          console.log(res.result)
          this.txtContent = res.result
          this.$refs.modalForm.view({
            publishlistId: this.publishlistId,
            title: '官网内容生成',
            content: res.result,
            isMarkDown: this.isMarkDown,
            templateType: 'Website'
          })
        } else {
          this.$message.error(res.message)
        }
      })
    }
  }
}
</script>

