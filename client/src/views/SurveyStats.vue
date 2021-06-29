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

        <div class="mt-4">
          <div v-for="response in surveyResponses" :key="response.id" class="mt-4">
            <h4>Response</h4>
            <span>id = {{ response.id }}</span>
            <div v-for="choiceResponse in response.choiceResponses" :key="choiceResponse.id">
              <span>question {{ choiceResponse.question.id }} -> choice {{ choiceResponse.choice.id }}</span>
            </div>
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
      surveyResponses: []
    }
  },
  props: {
    surveyUuid: String
  },
  created() {
    this.getSurveyByIdApi(this.surveyUuid).then(survey => {
      this.initSurveyQuestions(survey.questions)
    })

    // rsocket stuff
    console.log("connecting with RSocket..."); // TODO remove
    const transport = new RSocketWebSocketClient(
        {
          url: "ws://localhost:8082/rsocket"
        },
        // BufferEncoders
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

        console.log("connected to rsocket"); // TODO remove
        this.rsocket = socket;
        const endpoint = "api.v1.survey-response.stream"
        this.rsocket.requestStream({
          metadata: String.fromCharCode(endpoint.length) + endpoint
        })
        .subscribe({
          /*
            we create an infinite stream and to do so, in the callback
            of the onNext event we request new messages every time
            the client received the previously requested n messages
          */
          onSubscribe: (sub) => {
            console.log("subscribed to server stream"); // TODO remove
            this.requestStreamSubscription = sub
            this.requestStreamSubscription.request(requestedMsg)
          },
          onNext: (e) => {
            console.log("onNext", e); // TODO remove
            this.surveyResponses.push(e.data)
            // TODO handle incoming data
            processedMsg++;
            if (processedMsg >= requestedMsg) {
              this.requestStreamSubscription.request(requestedMsg);
              processedMsg = 0;
            }
          },
          onError: error => {
            console.log("got error with requestStream"); // TODO remove
            console.error(error);
          },
          onComplete: () => {
            console.log("requestStream completed"); // TODO remove
          }
        });
      },
      onError: error => {
        console.log("got connection error"); // TODO remove
        console.error(error);
      },
      // eslint-disable-next-line no-unused-vars
      onSubscribe: cancel => {
        /* call cancel() to abort */
      }
    });
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
    }
  }
}

</script>

<style>
</style>
