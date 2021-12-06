/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.w4lle.pulldownview.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.window.dialog.ToastDialog;
import com.w4lle.library.PullDownView;
import com.w4lle.pulldownview.ResourceTable;

/**
 * Sample app to test the PullDownView library functionality.
 */
public class MainAbilitySlice extends AbilitySlice {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        PullDownView pullDownView = (PullDownView) findComponentById(ResourceTable.Id_pull_down_view);
        pullDownView.setOnPullChangeListerner(new PullDownView.OnPullChangeListerner() {
            @Override
            public void onPullDown() {
                ToastDialog toastDialog = new ToastDialog(getContext());
                toastDialog.setText("onPullDown");
                toastDialog.show();
            }

            @Override
            public void onPullUp() {
                ToastDialog toastDialog = new ToastDialog(getContext());
                toastDialog.setText("onPullUp");
                toastDialog.show();
            }
        });
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}