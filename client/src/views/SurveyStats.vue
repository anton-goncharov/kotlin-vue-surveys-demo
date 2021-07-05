<template>
  <div class="row">
    <div class="col">
    </div>
    <div v-if="this.survey" class="col-lg-6 col-md-8 col-sm-10">
      <form class="container-fluid">
        <!-- Survey Title -->
        <div class="row">
          <div class="col-md-auto">
            <h3>{{ this.survey.title }}</h3>
          </div>
        </div>

        <!-- Cover Image-->
        <div v-if="survey.imageUrl" class="mt-2" style="width: 400px; height: 200px; background-size: cover;" v-bind:style="{ backgroundImage: 'url(\'' + this.$apiUrl + survey.imageUrl + '\')' }">
        </div>

        <div v-for="(question,qIndex) in this.survey.questions" v-bind:key="question.uuid" class="survey-question-block mt-5">
          <div class="row">
            <div class="col-md-8">
              <h5>{{ qIndex+1 }}. {{ question.text }}</h5>
            </div>
          </div>
          <div>
            <apexchart v-if="chartSeries[qIndex]" :ref="'qChart'+qIndex"
                       width="500"
                       type="bar"
                       :options="chartOptions[qIndex]"
                       :series="chartSeries[qIndex]">
            </apexchart>
          </div>
        </div>

        <div class="mt-4">
          <div v-for="response in this.surveyResponses" :key="response.id" class="mt-4">
            <!--     debug       -->
          </div>
        </div>

        <!-- Survey Controls -->
        <div aria-label="Toolbar with button groups" class="btn-toolbar my-5" role="toolbar">
          <button class="btn btn-light mr-2" type="button" v-on:click="close()">Close</button>
        </div>
      </form>
    </div>

    <!-- Loading Spinner   -->
    <div v-else class="col-lg-6 col-md-8 col-sm-10">
      <img src="data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA==" />
    </div>

    <div class="col">
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import {router} from "@/router";
import { RSocketClient } from "rsocket-core";
import RSocketWebSocketClient from "rsocket-websocket-client";
import { IdentitySerializer, JsonSerializer } from "rsocket-core/build";

export default {
  name: 'SurveyStats',
  components: {},
  data: function() {
    return {
      surveyImage: null,
      surveyResponses: [],
      chartOptions: [],
      chartSeries: [],
      responseCounter: []
    }
  },
  props: {
    surveyUuid: String
  },
  created() {

    function chartTemplate(survey, index) {
      return {
        chart: {
          id: 'vuechart-question-' + index,
          toolbar: {
            show: false
          },
        },
        colors: ['#FF9800'],
        xaxis: {
          categories: survey.questions[index].choices.map(c => c.text),
          max: 100,
          tickAmount: 5,
          labels: {
            formatter: (value) => value.toFixed(0) +'%'
          }
        },
        dataLabels: {
          formatter: (value) => value.toFixed(0) +'%'
        },
        plotOptions: {
          bar: {
            horizontal: true
          }
        }
      }
    }

    this.getSurveyByIdApi(this.surveyUuid).then(survey => {
      this.initSurveyQuestions(survey.questions).then(() => {
        // init charts
        for (const index in survey.questions) {
          this.chartOptions[index] = chartTemplate(survey, index)
          this.responseCounter[index] = { total: 0, choices: new Array(survey.questions[index].choices.length).fill(0) }
          this.chartSeries[index] = [{ data: [survey.questions[index].choices.length].fill(0) }]
        }
        // open stream
        this.openSurveyResponseStream()
      })
    })
  },
  computed: {
    ...mapState({
      survey: state => state.surveys.selected.item,
      surveyQuestions: state => state.surveyQuestions.all.items
    })
  },
  methods: {
    ...mapActions('surveys', {
      getSurveyByIdApi: 'getById'
    }),
    ...mapActions('surveyQuestions', {
      initSurveyQuestions: 'init'
    }),
    ...mapActions('surveyResponses', {
    }),

    // SURVEY
    close() {
      router.back()
    },

    // RSOCKET
    openSurveyResponseStream() {
      // rsocket stuff
      const transport = new RSocketWebSocketClient(
          {
            url: `${process.env.VUE_APP_BACKEND_WS_URL}/rsocket`
          }
      );
      const client = new RSocketClient({
        // send/receive JSON objects instead of strings/buffers
        serializers: {
          data: JsonSerializer,
          metadata: IdentitySerializer
        },
        setup: {
          // ms btw sending keepalive to server
          keepAlive: 60000,
          // ms timeout if no keep-alive response
          lifetime: 180000,
          dataMimeType: "application/json",
          metadataMimeType: 'message/x.rsocket.routing.v0'
        },
        transport
      });
      client.connect().subscribe({
        onComplete: socket => {
          let requestedMsg = 10;
          let processedMsg = 0;

          // console.log("connected to rsocket"); // debug
          const endpoint = "api.v1.survey-response.stream"
          socket.requestStream({
            data: {surveyUuid: this.survey.uuid},
            metadata: String.fromCharCode(endpoint.length) + endpoint
          })
              .subscribe({
                /*
                  we create an infinite stream and to do so, in the callback
                  of the onNext event we request new messages every time
                  the client received the previously requested n messages
                */
                onSubscribe: (sub) => {
                  console.log("subscribed to server stream"); // debug
                  this.requestStreamSubscription = sub
                  this.requestStreamSubscription.request(requestedMsg)
                },
                onNext: (e) => {
                  this.surveyResponses.push(e.data)
                  // handle incoming data, update series in the corresponding chart
                  this.addSurveyResponseToSeries(e.data)
                  console.log("next"); // TODO delete before commit
                  // count processed messages, when the buffer is full, request more from the socket
                  processedMsg++;
                  if (processedMsg >= requestedMsg) {
                    this.requestStreamSubscription.request(requestedMsg);
                    processedMsg = 0;
                  }
                },
                onError: error => {
                  // console.log("got error with requestStream"); // debug
                  console.error(error);
                },
                onComplete: () => {
                  // console.log("requestStream completed"); // debug
                }
              });
        },
        onError: error => {
          // console.log("got connection error"); // debug
          console.error(error);
        },
        // eslint-disable-next-line no-unused-vars
        onSubscribe: cancel => {
          /* call cancel() to abort */
        }
      });
    },

    addSurveyResponseToSeries(surveyResponse) {
      for (const choiceResponse of surveyResponse.choiceResponses) {
        for (const qIndex in this.surveyQuestions) {
          // find question
          if (this.surveyQuestions[qIndex].uuid === choiceResponse.question.id) {
            // find choice
            const questionChoices = this.surveyQuestions[qIndex].choices
            // in series, increase counter for the given question+choice
            for (const cIndex in questionChoices) {
              if (questionChoices[cIndex].uuid === choiceResponse.choice.id) {
                this.responseCounter[qIndex].total++;
                this.responseCounter[qIndex].choices[cIndex]++;
              }
            }
          }
        }
      }

      // recalculate
      for (const qIndex in this.surveyQuestions) {
        const questionChoices = this.surveyQuestions[qIndex].choices
        const total = this.responseCounter[qIndex].total;
        for (const cIndex in questionChoices) {
          const thisChoice = this.responseCounter[qIndex].choices[cIndex];
          this.chartSeries[qIndex][0].data[cIndex] = (thisChoice / total) * 100;

          // update chart
          if (this.$refs["qChart" + qIndex]) {
            this.$refs["qChart" + qIndex][0].updateSeries(this.chartSeries[qIndex], true);
          }
        }
      }

    }
  }
}

</script>

<style>
</style>
