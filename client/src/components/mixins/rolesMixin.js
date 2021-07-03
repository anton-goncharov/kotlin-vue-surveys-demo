import {mapState} from "vuex";

export default {
    computed: {
        ...mapState({
            account: state => state.account,
        })
    },
    methods: {
        isCoordinator() {
            return this.account.user.role === 'COORDINATOR'
        }
    }
}
