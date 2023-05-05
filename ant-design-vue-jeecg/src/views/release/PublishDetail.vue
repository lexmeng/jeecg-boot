<template>
  <page-layout :title="'发布单：' + publishForm.name" logo="https://gw.alipayobjects.com/zos/rmsportal/nxkuOJlFJuAUhzlMTCEe.png">
    <detail-list slot="headerContent" size="small" :col="2" class="detail-layout">
      <detail-list-item term="创建人">{{ publishForm.createBy }}</detail-list-item>
      <detail-list-item term="产品线">{{ publishForm.productLineId + '/' +  publishForm.productLineName }}</detail-list-item>
      <detail-list-item term="创建时间">{{ publishForm.createTime }}</detail-list-item>
      <detail-list-item term="产品">{{ publishForm.productName }}</detail-list-item>
      <detail-list-item term="迭代冲刺号">{{publishForm.scrumNum}}</detail-list-item>
      <detail-list-item term="版本类型">{{ publishForm.versionType }}</detail-list-item>
      <detail-list-item term="版本">{{publishForm.versionName}}</detail-list-item>
      <detail-list-item term="JIRA版本">{{ publishForm.jiraVersionName }}</detail-list-item>
    </detail-list>
    <a-row slot="extra" class="status-list">
      <a-col :xs="12" :sm="12">
        <div class="text">发布状态</div>
        <div class="heading">{{ publishForm.publishlistStage }}</div>
      </a-col>
      <a-col :xs="12" :sm="12">
        <div class="text">版本类型</div>
        <div class="heading">{{ publishForm.versionType }}</div>
      </a-col>
    </a-row>
    <!-- actions -->
    <template slot="action">
      <a-button-group style="margin-right: 4px;">
        <a-button>ReleaseNote</a-button>
        <a-button>ReleaseMail</a-button>
        <a-button>PR 文本</a-button>
        <a-button>手册 PR 文本</a-button>
      </a-button-group>
      <a-button type="primary">发布单详情</a-button>
    </template>

    <a-card :bordered="false" title="Sprint 迭代进度">
      <a-steps :direction="isMobile() && 'vertical' || 'horizontal'" :current="sprintCurrent" progressDot>
        <a-step v-for="item in sprintStages" :title="item.text"></a-step>
      </a-steps>
    </a-card>

    <a-card class="card" style="margin-top: 24px" :title="txtTitle" :bordered="false">
      <j-markdown-editor v-model="txtContent"></j-markdown-editor>
    </a-card>
  </page-layout>
</template>
<script>
import JMarkdownEditor from '@comp/jeecg/JMarkdownEditor/index'
import PageLayout from '@comp/page/PageLayout'
import DetailList from '@comp/tools/DetailList'
import { mixinDevice } from '@/utils/mixin'
import PublishlistForm from './modules/PublishlistForm'
import { initDictOptions } from '@comp/dict/JDictSelectUtil'

const DetailListItem = DetailList.Item

export default {
  name: 'PublishDetail',
  components: {
    PageLayout,
    DetailList,
    DetailListItem,
    JMarkdownEditor,
    PublishlistForm
  },
  mixins: [mixinDevice],
  data() {
    return {
      title: '',
      width: 800,
      visible: false,
      disableSubmit: false,
      txtTitle: '文本信息',
      txtContent: '',
      publishForm: {},
      publishStats: {},
      sprintStages: {}
    }
  },
  created() {
    this.publishForm = this.$route.query.record
    this.initDictConfig()
  },
  computed: {
    sprintCurrent() {
      let ss = Object.keys(this.sprintStages).map((key) => this.sprintStages[key].text);
      console.log('ss', ss, this.publishForm.scrumStage, ss.indexOf(this.publishForm.scrumStage))
      return ss.indexOf(this.publishForm.scrumStage)
    }
  },
  methods: {
    initDictConfig(){
      //初始化字典 - 性别
      initDictOptions('release_form_state').then((res) => {
        if (res.success) {
          this.publishStats = res.result;
          console.log('publishStats', this.publishStats)
        }
      });
      //初始化字典 - 性别
      initDictOptions('sprint_stage').then((res) => {
        if (res.success) {
          this.sprintStages = res.result;
          console.log('sprintStages', this.sprintStages)
        }
      });
    },
    add() {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.realForm.add()
      })
    },
    edit(record) {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.realForm.edit(record)
      })
    },
    submitCallback() {
      this.$emit('ok')
      this.visible = false
    }
  }
}
</script>


<style lang="less" scoped>

.detail-layout {
  margin-left: 44px;
}

.text {
  color: rgba(0, 0, 0, .45);
}

.heading {
  color: rgba(0, 0, 0, .85);
  font-size: 20px;
}

.no-data {
  color: rgba(0, 0, 0, .25);
  text-align: center;
  line-height: 64px;
  font-size: 16px;

  i {
    font-size: 24px;
    margin-right: 16px;
    position: relative;
    top: 3px;
  }
}

.mobile {
  .detail-layout {
    margin-left: unset;
  }

  .text {

  }

  .status-list {
    text-align: left;
  }
}
</style>