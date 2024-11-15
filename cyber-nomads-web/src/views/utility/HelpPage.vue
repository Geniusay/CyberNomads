<template>
  <div>
    <v-card theme="dark" class="search-card">
      <v-text-field
        label="search"
        :loading="loading"
        append-inner-icon="mdi-magnify"
        @click:append-inner="search"
        v-model="searchKey"
      ></v-text-field>
    </v-card>

    <v-row>
      <v-col cols="12" md="3" xl="2">
        <div class="text-subtitle-1 my-5 ml-2">Category</div>
        <div v-for="faq in questions" :key="faq.groupTitle">
          <v-btn variant="text" color="primary" @click="goTo(faq.groupTitle)">{{
            faq.groupTitle
          }}</v-btn>
        </div>
      </v-col>
      <v-col cols="12" md="9" xl="10">
        <div v-for="faq in questions" :id="faq.groupTitle" :key="faq.groupTitle">
          <div id="#general" class="text-subtitle-2 my-5 ml-2">
            {{ faq.groupTitle }}
          </div>
          <v-expansion-panels>
            <v-expansion-panel
              v-for="(item, index) in faq.questionList"
              :key="index"
              :title="item.question"
              :text="item.answer"
            ></v-expansion-panel>
          </v-expansion-panels>
        </div>
      </v-col>
    </v-row>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import { getQAList } from '@/api/utility'
import { reactive, ref, onMounted } from 'vue'
interface QuestionItem{
  question: string;
  answer: string;
}

interface Question {
  groupTitle: string;
  questionList: Array<QuestionItem>;
  englishQuestion: string;
  englishAnswer: string;
}

const questions = reactive<Array<Question>>([])

const router = useRouter();
const searchKey = ref("");
const loaded = ref(false);
const loading = ref(false);
const faqs = ref([
  {
    id: "general",
    title: "General Resources",
    items: [
      {
        title:
          "Can I use a purchased item in a freelance project or contract work for a client?",
        content:
          "Yes. However, if the client intends to charge End Users in any way from the End Product you create, you will need to purchase an Extended License. If you create the End Product for a client, your rights to purchased Items are transferred from you to your client.",
      },
      {
        title: "What is an End Product?",
        content:
          "An End Product is work that is designed or developed for a single, paid client. This website can not be resold as a product to multiple users. For more information on selling products to multiple users.",
      },
      {
        title: "What are the End Product requirements?",
        content:
          "An End Product must be a unique implementation of the Item, often requiring limited copy and content changes. For example, if you purchase a resume template, you may use the Item for yourself or a client after having input personal information (you may not resell it as stock).",
      },
      {
        title:
          "What is Personal Use, Commerical Work, Contracted Work, Client Work, etc.?",
        content:
          "If the created site can not charge users in any way, it is considered for Personal Use and a Regular License can be used. For End Products that can charge users, such as a Software as a Service application, or an e-commerce site, you should use an Extended License. For any End Products that will be sold in its entirety, such as creating software that is distributed digitally, use an Unlimited License.",
      },
      {
        title: "What is Personal Use?",
        content:
          "A Personal Use License can only be used for 1 End Product that does not charge users in any way.",
      },
      {
        title: "What is Commerical Use?",
        content:
          "A Commercial Use License can only be used for 1 End Product that charges or will charge users.",
      },
      {
        title: "What is Unlimited Use?",
        content:
          "An Extended Use License can be used for any number of Personal and Commercial projects.",
      },
    ],
  },
  {
    id: "licenses",
    title: "Licenses",
    items: [
      {
        title: "Personal Use",
        content:
          "Digital products purchased under the Standard License may be used one time in an End Product for Personal Use (an End Product that does not charge End Users).",
      },
      {
        title: "Commercial Use",
        content:
          "Digital products purchased under the Extended License may be used an unlimited amount of times for Personal Use, and one time to create a single End Product that does charge End Users.",
      },
      {
        title: "Unlimited Use",
        content:
          "An Unlimited License can be used an unlimited amount of times for Personal Use, Commercial Use, and Commercial End Products.",
      },
    ],
  },
]);

const search = () => {
  loading.value = true;
  setTimeout(() => {
    loading.value = false;
    loaded.value = true;
  }, 2000);
};

const goTo = (id) => {
  document.getElementById(id).scrollIntoView({ behavior: "smooth" });
};


onMounted(async ()=>{
  const data =   (await getQAList()).data
  // 对groupTitle 进行分组
  const groupedData = data.reduce((acc, item) => {
    const groupTitle = item.groupTitle;
    if( ! acc[groupTitle]) {
      acc[groupTitle] = {
        groupTitle: groupTitle,
        questionList: []
      };
    }
    acc[groupTitle].questionList.push({
      question: item.question,
      answer: item.answer,
      englishQuestion: item.englishQuestion,
      englishAnswer: item.englishAnswer,
    });
    return acc
  }, {})
  console.log(groupedData)
  // 将分组结果转换为数组形式
  const transformed = Object.values(groupedData);
  questions.splice(0, transformed.length, ...transformed);

  // 更新响应式数据
  console.log(questions)
})

</script>

<style scoped>
.search-card {
  background-image: linear-gradient(135deg, #141e30, #243b55);
  padding: 2rem;
}
</style>
