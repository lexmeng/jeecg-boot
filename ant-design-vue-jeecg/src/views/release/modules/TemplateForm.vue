<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :lg="12" :md="12" :sm="24" :span="24">
            <a-form-model-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="type">
              <!--              <a-input v-model="model.type" placeholder="请输入类型"  ></a-input>-->
              <j-dict-select-tag type="list" v-model="model.type" dictCode="template_type"
                                 placeholder="请选择模板类型" />
            </a-form-model-item>
          </a-col>
          <a-col :lg="12" :md="12" :sm="24" :span="24">
            <a-form-model-item label="产品线名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="productLineName">
              <j-dict-select-tag type="list" v-model="model.productLineName" dictCode="dict_bu"
                                 placeholder="请选择产品线" />
            </a-form-model-item>
          </a-col>
          <a-col :lg="12" :md="12" :sm="24" :span="24">
            <a-form-model-item label="产品" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="productName">
              <j-dict-select-tag type="list" v-model="model.productName" dictCode="product" placeholder="请选择产品" />
            </a-form-model-item>
          </a-col>
          <a-col :lg="12" :md="12" :sm="24" :span="24">
            <a-form-model-item label="文档版本" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="documentVersion">
              <a-input v-model="model.documentVersion" placeholder="请输入文档版本"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :lg="24" :md="24" :sm="24" :span="24">
            <a-form-model-item label="内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="content">
              <a-alert type="success">
                <span slot="message">
                  模板编写说明文档: <a target="_blank" href="https://kyligence.feishu.cn/wiki/wikcntChvZy7stn4BHd026mkSNc">DevOps平台占位符使用说明</a>
                </span>
              </a-alert>
              <j-editor v-if="model.type=='ReleaseMail'" v-model="model.content" height="500px"></j-editor>
              <j-markdown-editor v-else v-model="model.content" height="500px"></j-markdown-editor>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

import { httpAction, getAction } from '@/api/manage'
import { validateDuplicateValue } from '@/utils/util'

export default {
  name: 'TemplateForm',
  components: {},
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  data() {
    return {
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      confirmLoading: false,
      validatorRules: {
        type: [
          { required: true, message: '请输入类型!' }
        ],
        productLineName: [
          { required: true, message: '请输入产品线!' }
        ],
        productName: [
          { required: true, message: '请输入产品名!' }
        ],
        documentVersion: [
          { required: true, message: '请输入文档版本!' }
        ],
        content: [
          { required: true, message: '请输入内容!' }
        ]
      },
      url: {
        add: '/release/template/add',
        edit: '/release/template/edit',
        queryById: '/release/template/queryById'
      }
    }
  },
  computed: {
    formDisabled() {
      return this.disabled
    }
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
  },
  methods: {
    add() {
      this.edit(this.modelDefault)
    },
    edit(record) {
      this.model = Object.assign({}, record)
      this.visible = true
    },
    submitForm() {
      const that = this
      // 触发表单验证
      this.$refs.form.validate(valid => {
        if (valid) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          if (!this.model.id) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
          httpAction(httpurl, this.model, method).then((res) => {
            if (res.success) {
              that.$message.success(res.message)
              that.$emit('ok')
            } else {
              that.$message.warning(res.message)
            }
          }).finally(() => {
            that.confirmLoading = false
          })
        }
      })
    }
  }
}
</script>