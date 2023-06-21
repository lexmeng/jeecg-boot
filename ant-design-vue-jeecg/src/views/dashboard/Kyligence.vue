<template>
    <page-layout :avatar="avatar">
        <div slot="headerContent">
            <div class="title">{{ timeFix }}，{{ nickname() }}<span class="welcome-text">，{{ welcome() }}</span></div>
            <div>前端工程师 | 蚂蚁金服 - 某某某事业群 - VUE平台</div>
        </div>
    </page-layout>
</template>


<script>

export default {
    name: "Kyligence",
    data() {
        avatar: ''
    },
    computed: {
      userInfo() {
        return this.$store.getters.userInfo
      }
    },
}