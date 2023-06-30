<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="固定资产编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fixedAssetsNumber">
              <a-input v-model="model.fixedAssetsNumber" placeholder="请输入固定资产编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equipmentName">
              <a-input v-model="model.equipmentName" placeholder="请输入设备名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备型号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equipmentModel">
              <a-input v-model="model.equipmentModel" placeholder="请输入设备型号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="使用状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="useState">
              <j-dict-select-tag type="list" v-model="model.useState" dictCode="use_type" placeholder="请选择使用状态" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="使用部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="useOrgCode">
              <j-select-depart v-model="model.useOrgCode" :trigger-change="true" placeholder="请选择使用部门" customReturnField="orgCode" :multi="false"></j-select-depart>
<!--              <a-input v-model="model.useOrgCode" placeholder="请输入使用部门"  ></a-input>-->
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="保管人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="useOwner">
              <j-select-multi-user v-model="model.useOwner" placeholder="请选择保管人员" :query-config="selectUserQueryConfig"/>
<!--              <a-input v-model="model.useOwner" placeholder="请输入保管人员"  ></a-input>-->
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="存放地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="site">
              <j-dict-select-tag type="list" v-model="model.site" dictCode="it_site" placeholder="请选择存放地点" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="邮箱" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equEmail">
              <a-input v-model="model.equEmail" placeholder="请输入邮箱"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备原值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equOriValue">
              <a-input-number v-model="model.equOriValue" placeholder="请输入设备原值" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备净值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equNetValue">
              <a-input-number v-model="model.equNetValue" placeholder="请输入设备净值" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="折旧期数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equYear">
              <a-input v-model="model.equYear" placeholder="请输入折旧期数"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备分类" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equipmentClass">
              <j-dict-select-tag type="list" v-model="model.equipmentClass" dictCode="equ_type" placeholder="请选择设备分类" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="itTime">
              <j-date placeholder="请选择时间" v-model="model.itTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remarks">-->
<!--              <a-input v-model="model.remarks" placeholder="请输入备注"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'ItFaForm',
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
          add: "/it/itFa/add",
          edit: "/it/itFa/edit",
          queryById: "/it/itFa/queryById"
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