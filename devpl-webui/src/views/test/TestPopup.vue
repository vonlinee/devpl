<script setup lang="ts">
import { useUserVisitPopup } from "@/hooks/user-visit-popup"
import { useEventNotificationPopup } from "@/hooks/event-notification-popup"

const { openWelcomePopup, openNewUserPopup } = useUserVisitPopup()

const { openNewEventNotificationPopup, openEventNotificationPopup } =
  useEventNotificationPopup()
</script>

<template>
  <!-- <button @click="openWelcomePopup">弹出欢迎弹窗</button>
  <button @click="openNewUserPopup">弹出新用户弹窗</button>
  <button @click="openNewEventNotificationPopup">弹出活动预告</button>
  <button @click="openEventNotificationPopup">弹出当前正在进行中的热门活动</button> -->
</template>
