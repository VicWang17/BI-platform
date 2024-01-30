import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import React from 'react';

const Footer: React.FC = () => {
  const defaultMessage = "AST21403 Group Project"
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      copyright = {`${defaultMessage}`}
      links={[
        {
          key: 'UOW College Hong Kong',
          title: 'UOW College Hong Kong',
          href: 'https://www.uowchk.edu.hk',
          blankTarget: true,
        },
        {
          key: 'github',
          title: <GithubOutlined />,
          href: 'https://github.com/VicWang17',
          blankTarget: true,
        },

      ]}
    />
  );
};

export default Footer;
