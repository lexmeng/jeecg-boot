<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="软件名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="softwareName">
              <j-dict-select-tag type="list" v-model="model.softwareName" dictCode="SOFTWARE_TYPE" placeholder="请选择软件名" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="月份" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="month">
              <a-input-number v-model="model.month" placeholder="请输入月份" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="年份" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="year">
              <a-input-number v-model="model.year" placeholder="请输入年份" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="成本" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cost">
              <a-input-number v-model="model.cost" placeholder="请输入成本" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="拥有者" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="owner">
              <j-select-user-by-dep v-model="model.owner" :multi="true" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="department">
              <j-select-depart v-model="model.department" :multi="true"  />
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
    name: 'ItSoftwareMonthlyCostForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
         },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          add: "/it/itSoftwareMonthlyCost/add",
          edit: "/it/itSoftwareMonthlyCost/edit",
          queryById: "/it/itSoftwareMonthlyCost/queryById"
        }
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
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
    }
  }
</script>