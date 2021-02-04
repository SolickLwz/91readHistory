package com.nine.one.yuedu.read.entity.cp;

import java.util.List;

public class BiKanVolume {


    private List<VolumesBean> volumes;

    public List<VolumesBean> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<VolumesBean> volumes) {
        this.volumes = volumes;
    }

    public static class VolumesBean {
        /**
         * volumeId : 1
         * volumeTitle : 前言
         * volumeType : 0
         * chapterCount  : 1000
         * chapters : [{"chapterId":1,"vip":1,"title":"我是标题","chapterNumber":1}]
         * volumeNumber : 1
         */

        private int volumeId;
        private String volumeTitle;
        private int volumeType;
        private int chapterCount;
        private int volumeNumber;
        private List<ChaptersBean> chapters;

        public int getVolumeId() {
            return volumeId;
        }

        public void setVolumeId(int volumeId) {
            this.volumeId = volumeId;
        }

        public String getVolumeTitle() {
            return volumeTitle;
        }

        public void setVolumeTitle(String volumeTitle) {
            this.volumeTitle = volumeTitle;
        }

        public int getVolumeType() {
            return volumeType;
        }

        public void setVolumeType(int volumeType) {
            this.volumeType = volumeType;
        }

        public int getChapterCount() {
            return chapterCount;
        }

        public void setChapterCount(int chapterCount) {
            this.chapterCount = chapterCount;
        }

        public int getVolumeNumber() {
            return volumeNumber;
        }

        public void setVolumeNumber(int volumeNumber) {
            this.volumeNumber = volumeNumber;
        }

        public List<ChaptersBean> getChapters() {
            return chapters;
        }

        public void setChapters(List<ChaptersBean> chapters) {
            this.chapters = chapters;
        }

        public static class ChaptersBean {
            /**
             * chapterId : 1
             * vip : 1
             * title : 我是标题
             * chapterNumber : 1
             */

            private int chapterId;
            private int vip;
            private String title;
            private int chapterNumber;

            public int getChapterId() {
                return chapterId;
            }

            public void setChapterId(int chapterId) {
                this.chapterId = chapterId;
            }

            public int getVip() {
                return vip;
            }

            public void setVip(int vip) {
                this.vip = vip;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getChapterNumber() {
                return chapterNumber;
            }

            public void setChapterNumber(int chapterNumber) {
                this.chapterNumber = chapterNumber;
            }
        }
    }
}
