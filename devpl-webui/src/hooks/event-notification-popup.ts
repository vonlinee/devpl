/**
 * 活动相关的弹窗
 */
import { h } from 'vue'
import { usePopup } from '@/hooks/popup'
import CustomPopupContainer from '@/components/popup/index.vue'
import EventNotification from '@/components/popup/event-notification.vue'
import NewEventAnnouncement from '@/components/popup/new-event-announcement.vue'
import ButtonClose from '@/components/popup/button-close.vue'

export const useEventNotificationPopup = () => {
  /**
   * 当前进行中的活动弹窗
   */
  const closeEventNotificationHandle = () => {
    console.log('close')
    eventNotificationPopup.destroy()
  }

  const eventNotificationPopup = usePopup(
    h(
      CustomPopupContainer,
      {
        title: '恭喜发财2',
        /** @close="() => {}" */
        onClose: closeEventNotificationHandle,
        onOk: () => {
          // 点击ok的操作
        },
        onCancel: () => {
          // 点击取消的操作
        },
      },
      {
        default: () => h(EventNotification),
        close: () => h(ButtonClose),
      }
    )
  )

  const openEventNotificationPopup = () => {
    eventNotificationPopup.show()
  }

  /**
   * 新活动预告
   */
  const closeNewEventAnnouncementHandle = () => {
    console.log('close')
    newEventNotificationPopup.destroy()
  }

  const newEventNotificationPopup = usePopup(
    h(
      CustomPopupContainer,
      {
        title: '新活动预告',
        /** @close="() => {}" */
        onClose: closeNewEventAnnouncementHandle,
        onOk: () => {
          // 点击新活动时的逻辑
        },
        onCancel: () => {
          // 取消新活动时的逻辑
        },
      },
      {
        default: () => h(NewEventAnnouncement),
        close: () => h(ButtonClose),
      }
    )
  )

  const openNewEventNotificationPopup = () => {
    newEventNotificationPopup.show()
  }

  return {
    openEventNotificationPopup,
    openNewEventNotificationPopup,
  }
}
