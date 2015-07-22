/**
 * 2015-1-12 investigation base class definition
 */
function QuestionOption(id, title, serial, selected){
    this.id = id;
    this.title = title;
    this.serial = serial;
    this.selected = selected || false;
}

function Question(id, title, serial, optionList, maxOption){
    this.id = id;
    this.title = title;
    this.serial = serial;
    this.maxOption = maxOption;
    this.optionList = optionList;
    this.addOption = function(option){
        if( !option ) return;
        this.optionList = this.optionList || [];
        this.optionList.push(option);
    };
    this.clearOption = function(){
        this.optionList = [];
    }
}

function Investigation(id, title, startTime, endTime, questionList){
    this.id = id;
    this.title = title;
    this.startTime = startTime;
    this.endTime = endTime;
    this.questionList = questionList;
    this.addQuestion = function(question){
        if( !question ) return;
        this.questionList = this.questionList || [];
        this.questionList.push(question);
    };
    this.getQuestion = function(id){
        for(var i=0; i<this.questionList.length; i++){
            var q = this.questionList[i];
            if( q && q.id == id){
                return q;
            }
        }
        return null;
    };
    this.clearQuestion = function(){
        this.questionList = [];
    };
    this.setQuestionSequnce = function(qId, sequence){
        for(var i=0; i<this.questionList.length; i++){
            var q = this.questionList[i];
            if( q && q.id == id){
                q.serial = sequence;
            }
        }
    }
}

/*�ʾ���*/
function InvestResult(investId, userId, duration, startTime){
    this.investId = investId;
    this.userId = userId;
    this.duration = duration;
    this.startTime = startTime;
    this.questionList = [];
    // �������𰸣�����ΪQuestionResult����
    this.addQuestionResult = function(questionResult){
        this.questionList.push(questionResult);
    }
}

/*�����*/
function QuestionResult(questionId, optionList){
    this.questionId = questionId;
    this.optionList = optionList || [];
}

/*�ʾ���ܽ��*/
function InvestigationStat(id, title, count, avgDuration){
    this.id = id;
    this.title = title;
    this.count = count;
    this.avgDuration = avgDuration;
    this.questionList = [];
    this.addQuestion = function(question){
        if( !question ) return;
        this.questionList.push(question);
    }
}

/*�ʾ�ѡ����ܽ��*/
function OptionStat(title, count){
    this.title = title;
    this.count = count;
}

/*�ʾ�������ܽ��*/
function QuestionStat(title){
    this.title = title;
    this.optionList = [];
    this.addOption = function(option){
        if(!option) return;
        this.optionList.push(option);
    }
}

/*�û��ļ���Ϣ*/
function InvestUser(userId, name, duration, startTime){
    this.userId = userId;
    this.name = name;
    this.duration = duration;
    this.startTime = startTime;
}