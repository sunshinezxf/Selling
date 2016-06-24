/**
 * Created by sunshine on 6/24/16.
 */
$(document).ready(function () {
    $.post(order_url, function (result) {
    	var isShow=false;
        var pay_status = result.pay;
        var deliver_4_order_item = result.deliver4OrderItem;
        var deliver_4_cus = result.deliver4Cus;
        $.each(pay_status,function(i,value) {
        	if(value!=null&&value!=""&&value!=0)
            	isShow=true;
        });
        $.each(deliver_4_order_item,function(i,value) {
        	if(value!=null&&value!=""&&value!=0)
            	isShow=true;
        });
        $.each(deliver_4_cus,function(i,value) {
        	if(value!=null&&value!=""&&value!=0)
            	isShow=true;
        });
        if(isShow){
        	$('#order_container').highcharts({
                chart: {
                    type: 'bar'
                },
                title: {
                    text: '平台订单状态汇总'
                },
                xAxis: {
                    categories: ['代理商订单付款状态', '订单项发货状态', '网络订单发货状态'],
                    title: {
                        text: null
                    }
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: '订单数(笔)',
                        align: 'high'
                    },
                    labels: {
                        overflow: 'justify'
                    }
                },
                tooltip: {
                    valueSuffix: '笔'
                },
                plotOptions: {
                    bar: {
                        dataLabels: {
                            enabled: true
                        }
                    }
                },
                legend: {
                    layout: 'vertical',
                    align: 'right',
                    verticalAlign: 'top',
                    x: -40,
                    y: 100,
                    floating: true,
                    borderWidth: 1,
                    backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
                    shadow: true
                },
                credits: {
                    enabled: false
                },
                series: [{
                    name: '尚未',
                    data: [pay_status[0], deliver_4_order_item[0], deliver_4_cus[0]]
                }, {
                    name: '完成',
                    data: [pay_status[1], deliver_4_order_item[1], deliver_4_cus[1]]
                }]
            });
        }else{
        	$('#order_container').html('<p>对不起，当前暂无订单数据！</p>');
        }
        
    });

    $.post(agent_url, function (result) {
    	var isShow=false;
        var grant = result.grant;
        $.each(grant,function(i,value) {
            if(value!=null&&value!=""&&value!=0)
            	isShow=true;
        });
        if(isShow){
        	$('#agent_container').highcharts({
                chart: {
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false
                },
                title: {
                    text: '平台代理申请统计'
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>}'
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: false
                        },
                        showInLegend: true
                    }
                },
                series: [{
                    type: 'pie',
                    name: '占比',
                    data: [
                        ['已审核通过代理商', grant[0]],
                        ['尚未审核代理商', grant[1]]
                    ]
                }]
            });
        }else{
        	$('#agent_container').html('<p>对不起，当前暂无代理商数据！</p>');
        }
        
    });
});