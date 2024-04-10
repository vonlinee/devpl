import { Select } from 'antd';
import DatabaseNavigation from './DatabaseNavigation';
import { useEffect, useState } from 'react';
import { apiListSelectableDataSources } from '@/services/datasource';

const onChange = (value: string) => {
  console.log(`selected ${value}`);
};

const onSearch = (value: string) => {
  console.log('search:', value);
};

// Filter `option.label` match the user type `input`
const filterOption = (input: string, option?: { label: string; value: string }) =>
  (option?.label ?? '').toLowerCase().includes(input.toLowerCase());

export default () => {
  const [dataSourceOptions, setDataSourceOptions] = useState<SelectOptionType[]>();

  useEffect(() => {
    apiListSelectableDataSources().then((res) => {
      const options = res.data.map((i: any) => {
        return {
          label: i.name,
          value: i.id,
        };
      });
      setDataSourceOptions(options);
    });
  }, []);

  return (
    <>
      <Select
        showSearch
        placeholder="选择数据源"
        optionFilterProp="children"
        onChange={onChange}
        onSearch={onSearch}
        filterOption={filterOption}
        options={dataSourceOptions}
      />
      <DatabaseNavigation dataSourceId={2}></DatabaseNavigation>
    </>
  );
};
