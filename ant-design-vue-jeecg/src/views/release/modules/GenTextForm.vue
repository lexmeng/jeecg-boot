<template>
  <a-spin :spinning="confirmLoading">
    <a-button v-if="showPrButton" style="margin-bottom: 10px;" type="primary" icon="cloud-upload" @click="publishPR">生成发布PR</a-button>
    <a-alert type="success" style="margin: 10px 0;">
      <span slot="message">
        模板编写说明文档: <a target="_blank" href="https://kyligence.feishu.cn/wiki/wikcntChvZy7stn4BHd026mkSNc">DevOps平台占位符使用说明</a>
      </span>
    </a-alert>
    <j-markdown-editor v-if="model.isMarkDown" v-model="model.content" height="500px"></j-markdown-editor>
    <j-editor v-else v-model="model.content" style="height:500px"></j-editor>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'IssueForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      },
    },
    data () {
      return {
        model:{},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        url: {
          productPackagePR: '/release/jenkinsCommitProductPackagePR',
          productHandbookPR: '/release/jenkinsCommitProductHandbookPR',
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
      showPrButton(){
        return _.includes(['ProductPackagePRContent','HandBookPRChContent','HandBookPREnContent'], this.model.templateType)
      }
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      view (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      publishPR() {
        console.log("publishPR====> ", this.showPrButton, this.model.templateType);
        let urlPr = ''
        switch (this.model.templateType) {
          case 'ProductPackagePRContent':
            urlPr = this.url.productPackagePR
            break;
          case 'HandBookPRChContent':
          case 'HandBookPREnContent':
            urlPr = this.url.productHandbookPR
            break;
        }
        getAction(urlPr, {id: this.model.publishlistId}).then((res)=>{
          if(res.success){
            console.log(res.result)
            this.$message.success(res.message)
          }else{
            this.$message.error(res.message)
          }
        })
      }
    }
  }
</script>

<style scoped lang="less">
  .tui-editor-defaultUI .te-switch-button{
    width: 120px !important;
  }
</style>