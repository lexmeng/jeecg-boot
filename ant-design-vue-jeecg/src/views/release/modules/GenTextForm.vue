<template>
  <a-spin :spinning="confirmLoading">
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
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      view (record) {
        console.log('record', record)
        this.model = Object.assign({}, record);
        this.visible = true;

        console.log('this.model', this.model)
      },
    }
  }
</script>

<style scoped lang="less">
  .tui-editor-defaultUI .te-switch-button{
    width: 120px !important;
  }
</style>