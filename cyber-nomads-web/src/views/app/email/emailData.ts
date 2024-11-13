export type Email = {
  id: number;
  read: boolean;
  starred: boolean;
  time: string;
  subject: string;
  title: string;
  content: string;
  labels: string[];
};

export const inboxList: Email[] = [

];

export const starredList: Email[] = [
  {
    id: 1,
    read: false,
    starred: true,
    time: "15 min",
    subject: "Brunch this weekend?",
    title: "Ali Connors",
    content:
      "I'll be in your neighborhood doing errands this weekend. Do you want to hang out?",
    labels: ["work"],
  },
];
