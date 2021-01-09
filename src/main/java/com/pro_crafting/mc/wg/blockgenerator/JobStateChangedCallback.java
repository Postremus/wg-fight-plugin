package com.pro_crafting.mc.wg.blockgenerator;


import com.pro_crafting.mc.wg.blockgenerator.job.Job;

public interface JobStateChangedCallback
{
	public void jobStateChanged(Job job, JobState fromState);
}
