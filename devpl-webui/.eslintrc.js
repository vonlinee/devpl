module.exports = {
	root: true,
	env: {
		node: true,
		'vue/setup-compiler-macros': true
	},
	extends: ['plugin:vue/vue3-recommended', 'plugin:prettier/recommended'],
	parser: 'vue-eslint-parser',
	parserOptions: {
		parser: '@typescript-eslint/parser',
		ecmaVersion: 2022,
		ecmaFeatures: {
			jsx: true
		}
	},
	// eslint 规则配置
	rules: {
		curly: 'error', // 控制语句需要大括号
		'vue/multi-word-component-names': 'off',
		'no-console': 'off', //在这禁止掉console报错检查
		'no-irregular-whitespace': 'off', //这禁止掉 空格报错检查

	}
}
