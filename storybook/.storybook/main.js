module.exports = {
  stories: [
    "../../ex-web/packages/components/src/**/*.stories.mdx",
    "../../ex-web/packages/components/src/**/*.stories.@(js|jsx|ts|tsx)",
  ],
  addons: [
    {
      name: "@storybook/addon-docs",
      options: {
        configureJSX: true,
        transcludeMarkdown: true,
      },
    },
    "@storybook/addon-links",
    "@storybook/addon-essentials",
    "storybook-react-i18next",
  ],
  features: {
    emotionAlias: false,
  },
};
