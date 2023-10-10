import service from '@/utils/request'

/**
 * 获取Mapper中的参数
 * @param content
 */
export const apiListFields = () => {
  return service.get('/api/field/page')
}
