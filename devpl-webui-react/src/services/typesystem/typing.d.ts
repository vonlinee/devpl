type DataTypeGroupItem = {
  id: number;
  typeGroupId: string;
  typeGroupName: string;
};

type DataTypeListItem = {
  id?: number;
  typeGroupId?: string;
  typeKey: string;
  typeName: string;
  valueType: string;
  minLength?: number;
  maxLength?: number;
  defaultValue?: string;
  precision?: string;
  internal?: boolean;
  remark?: string;
  operation?: number;
};
