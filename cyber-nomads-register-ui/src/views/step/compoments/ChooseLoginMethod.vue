<template>
  <v-card
      class="mx-auto"
      width="600"
      min-height="500"
  >
    <v-card-title class="d-flex justify-space-between align-center">
      <div class="text-h5 text-medium-emphasis ps-2">
        选择登录方式
      </div>
    </v-card-title>
    <v-card-text>
      <v-row style="margin-top: 20%" justify="center">
        <v-col cols="auto"  class="pa-2 ma-5">
          <v-btn
              height="72"
              min-width="164"
              append-icon="mdi-shield-key"
          >
            账号密码登录
          </v-btn>
        </v-col>

        <v-col cols="auto" class="pa-2 ma-5">
          <v-btn
              height="72"
              min-width="164"
              append-icon="mdi-cloud-arrow-down"
          >
            线上数据拉取
          </v-btn>
        </v-col>
      </v-row>
    </v-card-text>
  </v-card>
</template>
