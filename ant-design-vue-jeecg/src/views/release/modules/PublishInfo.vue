<template>
  <div>
    <detail-list slot="headerContent" size="small" :col="3" class="detail-layout">
      <detail-list-item term="创建人">{{ publishForm.createBy }}</detail-list-item>
      <detail-list-item term="产品线">{{ publishForm.productLineName }}</detail-list-item>
      <detail-list-item term="创建时间">{{ publishForm.createTime }}</detail-list-item>
      <detail-list-item term="产品">{{ publishForm.productName }}</detail-list-item>
      <detail-list-item term="迭代冲刺号">{{ publishForm.scrumNum }}</detail-list-item>
      <detail-list-item term="版本类型">{{ publishForm.versionType }}</detail-list-item>
      <detail-list-item term="版本">{{ publishForm.versionName }}</detail-list-item>
      <detail-list-item term="JIRA版本">{{ publishForm.jiraVersionName }}</detail-list-item>
    </detail-list>
    <!-- <a-row slot="extra" class="status-list">
      <a-col :xs="12" :sm="12">
        <div class="text">发布状态</div>
        <div class="heading">{{ publishForm.publishlistStage }}</div>
      </a-col>
      <a-col :xs="12" :sm="12">
        <div class="text">版本类型</div>
        <div class="heading">{{ publishForm.versionType }}</div>
      </a-col>
    </a-row> -->
  </div>
</template>

<script>
import PageLayout from '@comp/page/PageLayout'
import DetailList from '@comp/tools/DetailList'

const DetailListItem = DetailList.Item

export default {
  name: 'PublishInfo',
  components: {
    PageLayout,
    DetailList,
    DetailListItem
  },
  props: {
    publishForm: {
      type: Object,
      default: {},
      required: true
    },
  },
  data() {
    return {}
  },
  created() {},
  mounted() {},
  computed: {},
  methods: {}
}
</script>

<style lang="less" scoped>
.detail-layout {
  margin-left: 44px;
}

.text {
  color: rgba(0, 0, 0, 0.45);
}

.heading {
  color: rgba(0, 0, 0, 0.85);
  font-size: 20px;
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
