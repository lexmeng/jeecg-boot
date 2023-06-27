<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="关闭">
    <test-quard-job-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></test-quard-job-form>
  </j-modal>
</template>

<script>

  import TestQuardJobForm from './TestQuardJobForm'
  export default {
    name: 'TestQuardJobModal',
    components: {
      TestQuardJobForm
    },
    data () {
      return {
        title:'',
        width:1000,
        visible: false,
        disableSubmit: false,
        publishlistId: ''
      }
    },
    methods: {
      add (record) {
        console.log("TestQuardJobModal publishlistId: ",record.publishlistId)
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.add(record);
        })
      },
      edit (record) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.edit(record);
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        this.$refs.realForm.submitForm();
      },
      submitCallback(){
        this.$emit('ok');
        this.visible = false;
      },
      handleCancel () {
        this.close()
      }
    }
  }
</script>