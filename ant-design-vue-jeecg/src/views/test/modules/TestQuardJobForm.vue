<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label=" 发布单 id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="publishlistId">
              <a-input v-model="model.publishlistId" placeholder="请输入 发布单 id" disabled ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="测试类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quardType">
              <j-dict-select-tag type="list" @change="handleQuardType" v-model="model.quardType" dictCode="quard_type" placeholder="请选择测试类型" />
              <a-popover title="Quard测试类型" trigger="hover">
                <template slot="content">
                  <ul>
                    <li>Quard测试日报：包括全平台兼容性和全量测试</li> <!--KE4X_Daily-->
                    <li>全量测试：默认运行在azure北三环境</li> <!--KE4X_FULL-->
                    <li>兼容性测试：运行在全平台</li> <!--KE4X_COMPATIBILITY-->
                    <li>相邻版本升级回滚测试：运行在全平台</li> <!--KE4X_UPGRADE-->
                    <li>大版本升级回滚测试：运行在全平台</li> <!--KE4X_LARGE_SPAN_UPGRADE-->
                  </ul>
                </template>
                <div class="extra">
                  <a-icon type="exclamation-circle" /> Quard测试类型
                </div>
              </a-popover>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="测试平台版本" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quardPlatform">-->
<!--              <j-popup v-model="model.quardPlatform" code="quard_platform" field="name" orgFields="name" destFields="name" :multi="true"/>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="quard代码分支" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quardBranch">-->
<!--              <a-input v-model="model.quardBranch" placeholder="请输入quard代码分支"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="版本号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="targetVersion">
              <a-input v-model="model.targetVersion" placeholder="请输入目标版本号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-if="isUpgrade">
            <a-form-model-item label="升级回滚出发版本号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fromVersion">
              <a-input v-model="model.fromVersion" placeholder="请输入升级回滚版本号"  ></a-input>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="指定 case" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="selectTests">-->
<!--              <a-input v-model="model.selectTests" placeholder="请输入指定 case"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="包存放地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="packageUrl">
              <a-input v-model="model.packageUrl" placeholder="请输入包存放地址"  ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row v-if="disabled">
          <a-col :span="24">
            <a-form-model-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="createBy">
              <a-input v-model="model.createBy" placeholder="请输入创建人" disabled ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="创建日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="createTime">
              <j-date placeholder="请选择创建日期"  v-model="model.createTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="更新人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="updateBy">
              <a-input v-model="model.updateBy" placeholder="请输入更新人" disabled ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="更新日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="updateTime">
              <j-date placeholder="请选择更新日期"  v-model="model.updateTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysOrgCode">
              <a-input v-model="model.sysOrgCode" placeholder="请输入所属部门" disabled ></a-input>
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
    name: 'TestQuardJobForm',
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
          add: "/test/testQuardJob/add",
          edit: "/test/testQuardJob/edit",
          queryById: "/test/testQuardJob/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
      isUpgrade() {
        return (this.model.quardType == 'KE4X_UPGRADE' || this.model.quardType == 'KE4X_LARGE_SPAN_UPGRADE')
      }
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add (record) {
        this.edit(this.modelDefault);
        this.model.quardBranch = 'quard-4.x'
        this.model.publishlistId = record.publishlistId;
        this.model.targetVersion = record.version;
        this.model.packageUrl = record.packageUrl;
      },
      handleQuardType(value) {
        console.log('handleQuardType', value)
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