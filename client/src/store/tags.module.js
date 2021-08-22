import { baseCrud } from './base-crud.module';
import {tagService} from "@/service/tag.service";

const state = {
};

const actions = {
}

const mutations = {
}

export const tags = baseCrud(tagService, {
    namespaced: true,
    state,
    actions,
    mutations
});