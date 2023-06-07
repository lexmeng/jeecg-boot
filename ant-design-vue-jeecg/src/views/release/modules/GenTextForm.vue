<template>
  <a-spin :spinning="confirmLoading">
    <a-button v-if="showPrButton" style="margin-bottom: 10px;" type="primary" icon="cloud-upload"
      @click="publishPR">生成发布PR</a-button>
    <a-alert type="success" style="margin: 10px 0;">
      <span slot="message">
        模板编写说明文档: <a target="_blank"
          href="https://kyligence.feishu.cn/wiki/wikcntChvZy7stn4BHd026mkSNc">DevOps平台占位符使用说明</a>
      </span>
    </a-alert>
    <j-markdown-editor v-model="model.content" height="800px"></j-markdown-editor>
    <a-modal title="确认信息" :visible="prPackage.visible" @ok="pprHandleOk" @cancel="pprHandleCancel">
      <a-form-model ref="form">
        <a-row>
          <a-col :span="24">
            <a-form-model-item :label-col="12" :wrapper-col="12" label="插入 package.sh 脚本的行号：">
              <a-input-number :min="1" v-model="prPackage.lineNumber" :width="24" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
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
  data() {
    return {
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      prPackage: {
        visible: false,
        lineNumber: 52,
      },
      url: {
        productPackagePR: '/release/jenkinsCommitProductPackagePR',
        productHandbookPR: '/release/jenkinsCommitProductHandbookPR',
      }
    }
  },
  computed: {
    formDisabled() {
      return this.disabled
    },
    showPrButton() {
      return _.includes(['ProductPackagePRContent', 'HandBookPRChContent', 'HandBookPREnContent'], this.model.templateType)
    }
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
  },
  methods: {
    view(record) {
      this.model = Object.assign({}, record);
      this.visible = true;
    },
    pprHandleOk() {
      console.log('pprHandleOk', this.prPackage.lineNumber);
      getAction(this.url.productPackagePR, { id: this.model.publishlistId, rowNumInsert: this.prPackage.lineNumber }).then((res) => {
        if (res.success) {
          console.log(res.result)
          this.prPackage.visible = false
          this.$message.success(res.message)
        } else {
          this.$message.error(res.message)
        }
      })
    },
    pprHandleCancel() {
      this.prPackage.visible = false
    },
    publishPR() {
      console.log("publishPR====> ", this.showPrButton, this.model.templateType);
      switch (this.model.templateType) {
        case 'ProductPackagePRContent':
          this.prPackage.visible = true
          break;
        case 'HandBookPRChContent':
        case 'HandBookPREnContent':
          getAction(this.url.productHandbookPR, { id: this.model.publishlistId }).then((res) => {
            if (res.success) {
              console.log(res.result)
              this.$message.success(res.message)
            } else {
              this.$message.error(res.message)
            }
          })
          break;
      }
    }
  }
}
</script>

<style lang="less">
.tui-editor-defaultUI .te-switch-button {
  width: 120px !important;
}
</style>