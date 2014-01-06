package com.jsmobile.app;

import android.app.Application;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;

import com.jsmobile.data.LauncherLayout;
import com.jsmobile.data.LauncherPageData;
import com.jsmobile.widget.BaseElement;
import com.jsmobile.widget.ElementLayout;
import com.jsmobile.widget.Navigator;
import com.jsmobile.widget.PageContainer;
import com.jsmobile.widget.PageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by wangxin on 11/7/13.
 * 只有左右可以翻屏幕，所以这里只在左右键时处理切换屏幕的事件
 */
public class FocusController {
    private static FocusController mFocusController;
    private FocusController(){}
    public static FocusController getInstance(){
        if(mFocusController == null){
            mFocusController = new FocusController();
        }
        return mFocusController;
    }

    private ViewGroup mPanel;
    private ViewGroup mNavigator;
    private ViewGroup mRoot;

    private int mPageNum;

    public void setTargetViews(ViewGroup root, ViewGroup top, ViewGroup bottom){
        mRoot = root;
        mPanel = top;
        mNavigator = bottom;

        mPageNum = mPanel.getChildCount();

        mNavigator.requestFocus();
    }

    public boolean onKeyEvent(int keyCode, KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                if(mRoot.getFocusedChild() == mPanel){
                    PageView pageView = (PageView) mPanel.getFocusedChild();
                    if(pageView != null && pageView.getElementLayout() != null){
                        BaseElement element = (BaseElement) pageView.getElementLayout().getFocusedChild();
                        if(isBottomBoundary(element, pageView)){//TODO
                            ((Navigator)mNavigator).focusDownToChild((String) pageView.getTag());
                            return true;
                        }
                    }
                } else if(mRoot.getFocusedChild() == mNavigator){
                    return  true;
                }
            } else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                View pageView = ((PageContainer) mPanel).getCurrentView();
                int pageIndex = mPanel.indexOfChild(pageView);
                int nextIndex = (pageIndex + 1 + mPageNum) % mPageNum;
                int preIndex = (pageIndex -1 + mPageNum ) % mPageNum;

                if(mRoot.getFocusedChild() == mPanel){
                    if(pageView != null && ((PageView)pageView).getElementLayout() != null){
                        BaseElement element = (BaseElement)((PageView)pageView).getElementLayout().getFocusedChild();
                        if(isRightBoundary(element, (PageView) pageView)){//TODO
                            ((PageContainer)mPanel).showNextPage();
                            ((Navigator)mNavigator).selectionMoveToChild((String) pageView.getTag(), (String) mPanel.getChildAt(nextIndex).getTag());

                            PageView nextPageView = (PageView) mPanel.getChildAt(nextIndex);
                            if(!moveFocusToNextPage(element, nextPageView)){
                                ((Navigator)mNavigator).focusDownToChild((String) nextPageView.getTag());
                            }
                            return  true;
                        }
                    }
                } else if(mRoot.getFocusedChild() == mNavigator){
                    ((Navigator)mNavigator).focusMoveToChild((String) mPanel.getChildAt(nextIndex).getTag());
                    ((PageContainer)mPanel).showNextPage();
                    return true;
                }
            } else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                View pageView = ((PageContainer) mPanel).getCurrentView();
                int pageIndex = mPanel.indexOfChild(pageView);
                int nextIndex = (pageIndex + 1 + mPageNum) % mPageNum;
                int preIndex = (pageIndex -1 + mPageNum ) % mPageNum;

                if(mRoot.getFocusedChild() == mPanel){
                    if(pageView != null && ((PageView)pageView).getElementLayout() != null){
                        BaseElement element = (BaseElement)((PageView)pageView).getElementLayout().getFocusedChild();
                        if(isLeftBoundary(element, (PageView) pageView)){//TODO
                            ((PageContainer)mPanel).showPreviousPage();
                            ((Navigator)mNavigator).selectionMoveToChild((String) pageView.getTag(), (String) mPanel.getChildAt(preIndex).getTag());

                            PageView prePageView = (PageView) mPanel.getChildAt(preIndex);
                            if(!moveFocusToPrePage(element, prePageView)){
                                ((Navigator)mNavigator).focusDownToChild((String) prePageView.getTag());
                            }
                            return  true;
                        }
                    }
                } else if(mRoot.getFocusedChild() == mNavigator){
                    ((Navigator)mNavigator).focusMoveToChild((String) mPanel.getChildAt(preIndex).getTag());
                    ((PageContainer)mPanel).showPreviousPage();
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * return false代表没有符合要求的元素
     */
    private boolean moveFocusToNextPage(BaseElement baseElement, PageView pageView){
        int curTop = baseElement.getElementTop();
        int height = baseElement.getElementHeight();
        ElementLayout elementLayout = pageView.getElementLayout();

        JsMobileApplication app = (JsMobileApplication) pageView.getContext().getApplicationContext();
        LauncherLayout.PageLayoutInfo pageLayoutInfo = app.getLauncherLayout().getPageLayoutInfo((String) pageView.getTag());
        LauncherPageData pageData = app.getLauncherData().getPageData((String) pageView.getTag());

        LauncherLayout.ElementLayoutInfo[] resultArray = new LauncherLayout.ElementLayoutInfo[3];
        Collection<LauncherLayout.ElementLayoutInfo> elementLayoutInfos = pageLayoutInfo.getAllElements().values();
        //去掉没有焦点的项
        Iterator<LauncherLayout.ElementLayoutInfo> iterator = elementLayoutInfos.iterator();
        while(iterator.hasNext()){
            LauncherLayout.ElementLayoutInfo elementLayoutInfo = iterator.next();
            if(pageData.getElement(elementLayoutInfo.id).isCanFocus()){
                if(elementLayoutInfo.top == 1){
                    if(resultArray[0] == null){
                        resultArray[0] = elementLayoutInfo;
                    } else {
                        if(resultArray[0].left > elementLayoutInfo.left){
                            resultArray[0] = elementLayoutInfo;
                        }
                    }
                }

                if(elementLayoutInfo.top == 2 || (elementLayoutInfo.top == 1 && elementLayoutInfo.height > 1)){
                    if(resultArray[1] == null){
                        resultArray[1]= elementLayoutInfo;
                    } else {
                        if(resultArray[1].left > elementLayoutInfo.left){
                            resultArray[1] = elementLayoutInfo;
                        }
                    }
                }

                if(elementLayoutInfo.top == 3 || (elementLayoutInfo.top == 1 && elementLayoutInfo.height > 2) ||
                        (elementLayoutInfo.top == 2 && elementLayoutInfo.height > 1)){
                    if(resultArray[2] == null){
                        resultArray[2] = elementLayoutInfo;
                    } else {
                        if(resultArray[2].left > elementLayoutInfo.left){
                            resultArray[2] = elementLayoutInfo;
                        }
                    }
                }
            }
        }

        if(resultArray[0] == null && resultArray[1] == null && resultArray[2] == null)return false;

        //TODO   need to process resultArray element as null

        if(curTop == 1){
            if(resultArray[0] != null){
                elementLayout.getElementByPos(resultArray[0].left, resultArray[0].top).requestFocus();
            } else if(resultArray[1] != null){
                elementLayout.getElementByPos(resultArray[1].left, resultArray[1].top).requestFocus();
            } else if(resultArray[2] != null){
                elementLayout.getElementByPos(resultArray[2].left, resultArray[2].top).requestFocus();
            }
        } else if(curTop == 2){
            if(resultArray[1] != null){
                elementLayout.getElementByPos(resultArray[1].left, resultArray[1].top).requestFocus();
            } else if(resultArray[0] != null){
                elementLayout.getElementByPos(resultArray[0].left, resultArray[0].top).requestFocus();
            } else if(resultArray[2] != null){
                elementLayout.getElementByPos(resultArray[2].left, resultArray[2].top).requestFocus();
            }
        } else if(curTop == 3){
            if(resultArray[2] != null){
                elementLayout.getElementByPos(resultArray[2].left, resultArray[2].top).requestFocus();
            } else if(resultArray[1] != null){
                elementLayout.getElementByPos(resultArray[1].left, resultArray[1].top).requestFocus();
            } else if(resultArray[0] != null){
                elementLayout.getElementByPos(resultArray[0].left, resultArray[0].top).requestFocus();
            }
        }

        return true;
    }

    private boolean moveFocusToPrePage(BaseElement baseElement, PageView pageView){
        int curTop = baseElement.getElementTop();
        int height = baseElement.getElementHeight();
        ElementLayout elementLayout = pageView.getElementLayout();

        JsMobileApplication app = (JsMobileApplication) pageView.getContext().getApplicationContext();
        LauncherLayout.PageLayoutInfo pageLayoutInfo = app.getLauncherLayout().getPageLayoutInfo((String) pageView.getTag());
        LauncherPageData pageData = app.getLauncherData().getPageData((String) pageView.getTag());

        LauncherLayout.ElementLayoutInfo[] resultArray = new LauncherLayout.ElementLayoutInfo[3];
        Collection<LauncherLayout.ElementLayoutInfo> elementLayoutInfos = pageLayoutInfo.getAllElements().values();
        //去掉没有焦点的项
        Iterator<LauncherLayout.ElementLayoutInfo> iterator = elementLayoutInfos.iterator();
        while(iterator.hasNext()){
            LauncherLayout.ElementLayoutInfo elementLayoutInfo = iterator.next();
            if(pageData.getElement(elementLayoutInfo.id).isCanFocus()){
                if(elementLayoutInfo.top == 1){
                    if(resultArray[0] == null){
                        resultArray[0] = elementLayoutInfo;
                    } else {
                        if(resultArray[0].left < elementLayoutInfo.left){
                            resultArray[0] = elementLayoutInfo;
                        }
                    }
                }

                if(elementLayoutInfo.top == 2 || (elementLayoutInfo.top == 1 && elementLayoutInfo.height > 1)){
                    if(resultArray[1] == null){
                        resultArray[1]= elementLayoutInfo;
                    } else {
                        if(resultArray[1].left < elementLayoutInfo.left){
                            resultArray[1] = elementLayoutInfo;
                        }
                    }
                }

                if(elementLayoutInfo.top == 3 || (elementLayoutInfo.top == 1 && elementLayoutInfo.height > 2) ||
                        (elementLayoutInfo.top == 2 && elementLayoutInfo.height > 1)){
                    if(resultArray[2] == null){
                        resultArray[2] = elementLayoutInfo;
                    } else {
                        if(resultArray[2].left < elementLayoutInfo.left){
                            resultArray[2] = elementLayoutInfo;
                        }
                    }
                }
            }
        }

        if(resultArray[0] == null && resultArray[1] == null && resultArray[2] == null)return false;

        if(curTop == 1){
            if(resultArray[0] != null){
                elementLayout.getElementByPos(resultArray[0].left, resultArray[0].top).requestFocus();
            } else if(resultArray[1] != null){
                elementLayout.getElementByPos(resultArray[1].left, resultArray[1].top).requestFocus();
            } else if(resultArray[2] != null){
                elementLayout.getElementByPos(resultArray[2].left, resultArray[2].top).requestFocus();
            }
        } else if(curTop == 2){
            if(resultArray[1] != null){
                elementLayout.getElementByPos(resultArray[1].left, resultArray[1].top).requestFocus();
            } else if(resultArray[0] != null){
                elementLayout.getElementByPos(resultArray[0].left, resultArray[0].top).requestFocus();
            } else if(resultArray[2] != null){
                elementLayout.getElementByPos(resultArray[2].left, resultArray[2].top).requestFocus();
            }
        } else if(curTop == 3){
            if(resultArray[2] != null){
                elementLayout.getElementByPos(resultArray[2].left, resultArray[2].top).requestFocus();
            } else if(resultArray[1] != null){
                elementLayout.getElementByPos(resultArray[1].left, resultArray[1].top).requestFocus();
            } else if(resultArray[0] != null){
                elementLayout.getElementByPos(resultArray[0].left, resultArray[0].top).requestFocus();
            }
        }

        return true;
    }

    public static boolean isRightBoundary(BaseElement element, PageView page){
        int curTop = element.getElementTop();
        int curLeft = element.getElementLeft();
        int curWidth = element.getElementWidth();
        int curHeight = element.getElementHeight();
        String id = element.getElementId();

        JsMobileApplication app = (JsMobileApplication) element.getContext().getApplicationContext();
        LauncherLayout.PageLayoutInfo pageLayoutInfo = app.getLauncherLayout().getPageLayoutInfo((String) page.getTag());
        LauncherPageData pageData = app.getLauncherData().getPageData((String) page.getTag());

        Collection<LauncherLayout.ElementLayoutInfo> elementLayoutInfos = pageLayoutInfo.getAllElements().values();

        Iterator<LauncherLayout.ElementLayoutInfo> iterator = elementLayoutInfos.iterator();

        while (iterator.hasNext()){
            LauncherLayout.ElementLayoutInfo elementLayoutInfo = iterator.next();
            if(pageData.getElement(elementLayoutInfo.id).isCanFocus()){
                if(isOverlapVertical(curTop, curHeight, elementLayoutInfo.top, elementLayoutInfo.height) &&
                        (elementLayoutInfo.left >= (curLeft + curWidth))){
                    if(elementLayoutInfo.id.equalsIgnoreCase(element.getElementId())){
                        //can not happen
                    } else {
                        Log.d("wangxin", "isRightBoundary, " + String.format("cur left-top-width-height: %d-%d-%d-%d", curLeft, curTop, curWidth,curHeight) +
                            String.format(", element left-top-width-height: %d-%d-%d-%d", elementLayoutInfo.left, elementLayoutInfo.top, elementLayoutInfo.width,
                                    elementLayoutInfo.height));
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean isLeftBoundary(BaseElement element, PageView page){
        int curTop = element.getElementTop();
        int curLeft = element.getElementLeft();
        int curWidth = element.getElementWidth();
        int curHeight = element.getElementHeight();
        String id = element.getElementId();

        JsMobileApplication app = (JsMobileApplication) element.getContext().getApplicationContext();
        LauncherLayout.PageLayoutInfo pageLayoutInfo = app.getLauncherLayout().getPageLayoutInfo((String) page.getTag());
        LauncherPageData pageData = app.getLauncherData().getPageData((String) page.getTag());

        Collection<LauncherLayout.ElementLayoutInfo> elementLayoutInfos = pageLayoutInfo.getAllElements().values();

        Iterator<LauncherLayout.ElementLayoutInfo> iterator = elementLayoutInfos.iterator();

        while (iterator.hasNext()){
            LauncherLayout.ElementLayoutInfo elementLayoutInfo = iterator.next();
            if(pageData.getElement(elementLayoutInfo.id).isCanFocus()){
                if(isOverlapVertical(curTop, curHeight, elementLayoutInfo.top, elementLayoutInfo.height) &&
                        ((elementLayoutInfo.left + elementLayoutInfo.width) <= curLeft)){
                    if(elementLayoutInfo.id.equalsIgnoreCase(element.getElementId())){
                        //can not happen
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean isTopBoundary(BaseElement element, PageView page){
        int curTop = element.getElementTop();
        int curLeft = element.getElementLeft();
        int curWidth = element.getElementWidth();
        int curHeight = element.getElementHeight();
        String id = element.getElementId();

        JsMobileApplication app = (JsMobileApplication) element.getContext().getApplicationContext();
        LauncherLayout.PageLayoutInfo pageLayoutInfo = app.getLauncherLayout().getPageLayoutInfo((String) page.getTag());
        LauncherPageData pageData = app.getLauncherData().getPageData((String) page.getTag());

        Collection<LauncherLayout.ElementLayoutInfo> elementLayoutInfos = pageLayoutInfo.getAllElements().values();

        Iterator<LauncherLayout.ElementLayoutInfo> iterator = elementLayoutInfos.iterator();

        while (iterator.hasNext()){
            LauncherLayout.ElementLayoutInfo elementLayoutInfo = iterator.next();
            if(pageData.getElement(elementLayoutInfo.id).isCanFocus()){
                if(isOverlapHorizontal(curLeft, curWidth, elementLayoutInfo.left, elementLayoutInfo.width) &&
                        (elementLayoutInfo.top + elementLayoutInfo.height <= curTop)){
                    if(elementLayoutInfo.id.equalsIgnoreCase(element.getElementId())){
                        //can not happen
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean isBottomBoundary(BaseElement element, PageView page){
        int curTop = element.getElementTop();
        int curLeft = element.getElementLeft();
        int curWidth = element.getElementWidth();
        int curHeight = element.getElementHeight();
        String id = element.getElementId();

        JsMobileApplication app = (JsMobileApplication) element.getContext().getApplicationContext();
        LauncherLayout.PageLayoutInfo pageLayoutInfo = app.getLauncherLayout().getPageLayoutInfo((String) page.getTag());
        LauncherPageData pageData = app.getLauncherData().getPageData((String) page.getTag());

        Collection<LauncherLayout.ElementLayoutInfo> elementLayoutInfos = pageLayoutInfo.getAllElements().values();

        Iterator<LauncherLayout.ElementLayoutInfo> iterator = elementLayoutInfos.iterator();

        while (iterator.hasNext()){
            LauncherLayout.ElementLayoutInfo elementLayoutInfo = iterator.next();
            if(pageData.getElement(elementLayoutInfo.id).isCanFocus()){
                if(isOverlapHorizontal(curLeft, curWidth, elementLayoutInfo.left, elementLayoutInfo.width) &&
                        (elementLayoutInfo.top >= curTop + curHeight)){
                    if(elementLayoutInfo.id.equalsIgnoreCase(element.getElementId())){
                        //can not happen
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean isOverlapVertical(int curTop, int curHeight, int top, int height){
        if(curTop == top)return true;
        if(curTop > top){
            int bottom = top + height -1;
            if(curTop >= top && curTop <= bottom)return true;
        } else {
            int curBottom = curTop + curHeight - 1;
            if(top >= curTop && top <= curBottom)return true;
        }
        return false;
    }

    public static boolean isOverlapHorizontal(int curLeft, int curWidth, int left, int width){
        if(curLeft == left)return true;
        if(curLeft > left){
            int right = left + width - 1;
            if(curLeft >= left && curLeft <= right)return true;
        } else {
            int curRight = curLeft + curWidth - 1;
            if(left >= curLeft && left <= curRight)return true;
        }
        return false;
    }


}
